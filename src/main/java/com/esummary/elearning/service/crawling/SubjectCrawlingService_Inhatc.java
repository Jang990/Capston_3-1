package com.esummary.elearning.service.crawling;

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
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectRepository;
import com.esummary.elearning.service.crawling.notice.NoticeCrawlingService;
import com.esummary.elearning.service.crawling.task.TaskCrawlingService;
import com.esummary.elearning.service.crawling.user.UserCrawlingUtil;
import com.esummary.elearning.service.crawling.week.WeekCrawlingService;

@Component("Crawl")
public class SubjectCrawlingService_Inhatc implements SubjectCrawlingService{
	
	@Override
	public List<SubjectInfo> crawlBasicSubjectInfo(Map<String, String> loginCookie) {
		List<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
		Elements subjectElements = crawlSubjectElements(loginCookie);
		
		if(subjectElements.size() == 0) return null;
		
		for (Element subjectElement : subjectElements) {			
			SubjectInfo subject = convertElementToSubjectInfo(subjectElement);
			if(subject == null) continue;
			subjectList.add(subject);
		}
		
		return subjectList;
	}
	
	private Elements crawlSubjectElements(Map<String, String> loginCookie) {
		Document loginPage = this.conLoginPage(loginCookie);
		String subjectSelector = ".default option";
		return loginPage.select(subjectSelector);
	}

	private SubjectInfo convertElementToSubjectInfo(Element subjectElement) {
		if(subjectElement.attr("value") == null || subjectElement.attr("value").equals("")) 
			return null;
		
		final int ID_IDX = 0;
		final int OWNER_NAME_IDX = 1;
		final int OPEN_TYPE_IDX = 2;
		String[] subjectStringValues = subjectElement.attr("value").split(",");
		
		SubjectInfo subject = new SubjectInfo(
					subjectStringValues[ID_IDX].trim(), 
					subjectElement.text(), 
					subjectStringValues[OWNER_NAME_IDX].trim(), 
					subjectStringValues[OPEN_TYPE_IDX].trim()
				);
		return subject;
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
	
	// private으로 바꾸고 싶다
	private static String createStudyHomeUrl(String subjectId) {  
//		final String MOBILE_STUDY_HOME_URL1 = ELearningServiceImpl_test.MAIN_URL + "/MCourse.do?cmd=viewStudyHome&courseDTO.courseId=";
		final String DESKTOP_STUDY_HOME_URL1 = ELearningURL.MAIN_URL + "/Course.do?cmd=viewStudyHome&courseDTO.courseId=";
		final String STUDY_HOME_URL2 = "&boardInfoDTO.boardInfoGubun=study_home&boardGubun=study_course&gubun=study_course";
		
		return DESKTOP_STUDY_HOME_URL1 + subjectId + STUDY_HOME_URL2;
	}
	
	public static Document connStudyHome(String subjectId, Map<String, String> loginCookies) { 
		String studyHome = createStudyHomeUrl(subjectId);
		Document docStudyHome = null;
		try {
			docStudyHome = Jsoup.connect(studyHome).cookies(loginCookies).get();
		} catch (IOException e) {
			System.out.println("로그인 쿠키가 잘못된것 같다.");
			e.printStackTrace();
		}
		
		return docStudyHome;
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
