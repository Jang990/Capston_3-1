package com.esummary.elearning.service.subject.util.crawling;

import java.io.IOException;  
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.esummary.elearning.dto.SubjectDetailData_DTO;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;
import com.esummary.elearning.service.subject.util.crawling.lectures.LectureWeekUtil;
import com.esummary.elearning.service.subject.util.crawling.notice.NoticeUtil;
import com.esummary.elearning.service.subject.util.crawling.task.TaskUtil;
import com.esummary.elearning.service.user.crawling.UserCrawlingUtil;

@Component("Crawl")
public class SubjectUtil_Inhatc implements SubjectUtil{
	
	@Autowired
	@Qualifier("crawlTas")
	private TaskUtil taskUtil;
	@Autowired
	@Qualifier("crawlNot")
	private NoticeUtil noticeUtil;
	@Autowired
	@Qualifier("crawlLec")
	private LectureWeekUtil lectureWeekUtil;
	
	private static int seqUserSubjectNum = 0; // 시퀸스 넘버. UserSubject에서 복합키 사용전에 임의로 사용함. 복합키로 바꿀 것
	
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	
	public List<SubjectInfo> getSubjectList(UserInfo user) {
		//여길 사실상 쓰는데가 없다. 
		//summary에서 쓰나 사장되었고, test 또한 단위테스트용이라 지우면 그만이다.
		
		List<SubjectInfo> subjectList = this.getInitialSubjectData(user.getInitialCookies());
		subjectRepository.saveAll(subjectList);		//DB 저장
		user.setSubjectList(subjectList); //DB연동없이 하는것이기때문에 오류가 있을 수 있음?
		
		List<UserSubject> usList = this.createAndSaveUserSubject(user, subjectList);
		user.setUserSubjects(usList); //DB연동없이 하는것이기때문에 오류가 있을 수 있음?
		
		this.setSubjectDetail(user); // 여기서 user가 가지고 있는 subject에 대한 과제, 강의, 공지 정보들을 전부 세팅해준다.
		return user.getSubjectList();
	}
	
	public SubjectDetailData_DTO getSubjectDetail(UserData user, String subjectId) {
		//공지 크롤링 부분
		UserSubject userSubject = userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, user.getStudentNumber());
		List<SubjectNoticeInfo> notices = noticeUtil.getSubjectNoticeInfo(userSubject, user.getInitialCookies());
		
		//과제 크롤링 부분
//		UserSubject userSubject = userSubjectRepository.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, user.getStudentNumber());
		List<SubjectTaskInfo> task = taskUtil.getSubjectTaskInfo(userSubject, user.getInitialCookies());
		
		//강의 크롤링 부분
//		UserSubject userSubject = userSubjectRepository.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, user.getStudentNumber());
		List<SubjectLectureWeekInfo> lectures = lectureWeekUtil.getSubjectLectureWeekInfo(userSubject, user.getInitialCookies());
		
		return new SubjectDetailData_DTO(subjectId, lectures, task, notices);		
	}
	
	@Override
	public List<SubjectInfo> crawlAndSaveBasicSubjectData(UserInfo user) {
		List<SubjectInfo> subjectList = this.getInitialSubjectData(user.getInitialCookies());
		subjectRepository.saveAll(subjectList);		//DB 저장
		user.setSubjectList(subjectList); //DB연동없이 하는것이기때문에 오류가 있을 수 있음?
		return subjectList;
	}
	
	@Override
	public boolean saveBasicSubject(UserInfo user, List<SubjectInfo> subjectList) {
		List<UserSubject> usList = this.createAndSaveUserSubject(user,subjectList);
		if(usList.isEmpty()) {
			return false;
		}
		else {
			user.setUserSubjects(usList); //DB연동없이 하는것이기때문에 오류가 있을 수 있음?
			return true;
		}
	}
	
	private List<SubjectInfo> getInitialSubjectData(Map<String, String> loginCookie) {
		
		List<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
		String subjectSelector = ".default option";
		Document loginPage = this.conLoginPage(loginCookie);
		Elements subjectElements = loginPage.select(subjectSelector);
//		Elements subjectElements = loginPage.select(".default option");
		
		final int ID_IDX = 0;
		final int OWNER_NAME_IDX = 1;
		final int OPEN_TYPE_IDX = 2;
		
		for (Element subjectElement : subjectElements) {
			if(subjectElement.attr("value") == null || subjectElement.attr("value").equals("")) 
				continue;
			
			String[] subjectStringValues = subjectElement.attr("value").split(",");
			
			SubjectInfo subject = new SubjectInfo();
			subject.setSubjectName(subjectElement.text()); 
			subject.setSubjectId(subjectStringValues[ID_IDX].trim());
			subject.setSubjectOwnerName(subjectStringValues[OWNER_NAME_IDX].trim());
			subject.setOpenType(subjectStringValues[OPEN_TYPE_IDX].trim());
//			subjectRepository.save(subject);		//DB 저장
			subjectList.add(subject);
		}
		
		return subjectList;
	}
	
	private Document conLoginPage(Map<String, String> initialCookies) {
		//이거 이름 크롤링부분에 있는 메소드인데 재사용 어떡할지 못정해서 그냥 여기 박아버림
		String mainUrl = "https://cyber.inhatc.ac.kr" + "/MMain.do";
		Document loginPage = null;
		
		try {
			Connection con = Jsoup.connect(mainUrl)
					.data("cmd", "viewIndexPage")
					.cookies(initialCookies);
			Connection.Response resp = con.execute();
			
			if(resp.statusCode() == 200)
				loginPage = con.post();
			else
				return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loginPage;
	}
	
	private List<UserSubject> createAndSaveUserSubject(UserInfo user, List<SubjectInfo> subjectList) {
		//setUserSubject 및 UserSubject DB 저장
		List<UserSubject> usList = new ArrayList<UserSubject>();
		for (SubjectInfo subject : subjectList) {
			UserSubject us = new UserSubject();
			us.setUsId(seqUserSubjectNum++);
			us.setSubjectInfo(subject);
			us.setUserInfo(user);
			usList.add(us);
//			if(userSubjectRepository.
//					findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subject.getSubjectId(), user.getStudentNumber()) != null)
//			userSubjectRepository.save(us);
		}
		userSubjectRepository.saveAll(usList);
		return usList;
	}
	
	private void setSubjectDetail(UserInfo user) {
		Map<String, String> initialCookies = user.getInitialCookies();
		
//		user.getUserSubjects().get(0).get
		
		List<SubjectTaskInfo> taskList = new ArrayList<SubjectTaskInfo>();
		List<SubjectNoticeInfo> noticeList = new ArrayList<SubjectNoticeInfo>();
		List<SubjectLectureWeekInfo> lectureList = new ArrayList<SubjectLectureWeekInfo>();
		
		List<UserSubject> userSubjects = user.getUserSubjects();
		for (UserSubject userSubject : userSubjects) {
			SubjectInfo subjectInfo = userSubject.getSubjectInfo();
			
			String subjectId = subjectInfo.getSubjectId();
			String studyHome = createStudyHomeUrl(subjectId);
			Document docStudyHome = null;
			
			try {
				docStudyHome = Jsoup.connect(studyHome).cookies(initialCookies).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			taskList = taskUtil.getSubjectTaskInfo(userSubject, docStudyHome, initialCookies);
			noticeList = noticeUtil.getSubjectNoticeInfo(userSubject, docStudyHome, initialCookies);
			lectureList = lectureWeekUtil.getSubjectLectureInfo(userSubject, docStudyHome, initialCookies);
			
			
			/*
			 * 이상하게 taskUtil로 생성된 taskList를 SubjectInfo에 setTaskList를 하고
			 * subjectInfo를 task나 lecture같은 뒷부분에서 SubjectInfo를 사용하려하면 오류가 난다.
			 * lombok과 JPA를 함께 쓸 때 오류이다.
			 * https://lng1982.tistory.com/300
			 * 관련 에러 해설
			 */
			subjectInfo.setTaskList(taskList);
			subjectInfo.setNoticeList(noticeList);
			subjectInfo.setLectureList(lectureList);
		}
		
	}
	
	public static String createStudyHomeUrl(String subjectId) { 
//		final String MOBILE_STUDY_HOME_URL1 = ELearningServiceImpl_test.MAIN_URL + "/MCourse.do?cmd=viewStudyHome&courseDTO.courseId=";
		final String DESKTOP_STUDY_HOME_URL1 = ELearningServiceImpl.MAIN_URL + "/Course.do?cmd=viewStudyHome&courseDTO.courseId=";
		final String STUDY_HOME_URL2 = "&boardInfoDTO.boardInfoGubun=study_home&boardGubun=study_course&gubun=study_course";
		
		return DESKTOP_STUDY_HOME_URL1 + subjectId + STUDY_HOME_URL2;
	}
	
	public static String[] extractDataFromJsCode(String attr) {
		String unnecessaryString= "javascript:" + foundUnnecessaryString(attr) + "(";
		
		String[] taskData = attr.replace(unnecessaryString, "")
				.replace(");", "")
				.replace("'", "")
				.split(",");
		
		for(int i = 0; i < taskData.length; i++)
			taskData[i] = taskData[i].trim();
		
		return taskData;
	}

	private static String foundUnnecessaryString(String attr) {
		if(attr.contains("ViewReportContent"))
			return "ViewReportContent"; //과제일때
		else if(attr.contains("ViewBoardContent"))
			return "ViewBoardContent"; //공지사항일때
		else if(attr.contains("submitReportView"))
			return "submitReportView"; //??
		else if(attr.contains("submitReport"))
			return "submitReport"; //과제가 현재 진행중(제출하기 버튼일 때)
		else if(attr.contains("viewStudyContents")) //학습하기일때
			return "viewStudyContents";
//		else if(attr.contains("viewStudyBoard"))
//			return "viewStudyBoard"; //학습활동글쓰기일때 모르겠다 
		else if(attr.contains("moveBoardView"))
			return "moveBoardView";
		else
			return null;
	}
	
	/**
	 * "aaa" / "2022-02-27" / "~" / "2022-06-17" <br>
	 * 위와 같은 데이터에서 ~ 의 위치를 알아냄
	 * @param splitData 
	 * @return '~'의 인덱스번호. '~'가 없을 시 0을 반환.
	 */
	public static int findTildeIdx(String[] splitData) {
		int tildeIdx = 0;
		
		for (int i = 0; i < splitData.length; i++) {
			if(splitData[i].equals("~")) {
				tildeIdx = i;
				break;
			}
		}
		
		return tildeIdx;
	}
	
	public static Date parseDate(String dateString) {
		SimpleDateFormat sdFormat;
		if(dateString.contains(":"))
			sdFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm"); //과제
		else
			sdFormat = new SimpleDateFormat("yyyy-MM-dd"); //주차
		
		Date date = null;
		try {
			date = sdFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
}
