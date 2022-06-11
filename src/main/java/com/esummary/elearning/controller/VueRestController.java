package com.esummary.elearning.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.LectureData;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.subject.ELearningService;
import com.esummary.elearning.service.test.TestService;
import com.esummary.elearning.service.vue.VueService;

@RestController
public class VueRestController {
	
	@Autowired
	private VueService vueService;
	
	@Autowired
	private LoginService eLearningLoginService;
	@Autowired
	private ELearningService eLearningService;
	
	//로그인 체크
	@RequestMapping("/vueLoginCheck") 
	public boolean loginCheck(@RequestParam String id, @RequestParam String password, HttpServletRequest request) {
		UserInfo user = eLearningLoginService.login(id, password);
		if(user == null) {
			return false;  
		}   
		else {
			UserData userData = new UserData(
						user.getStudentNumber(), 
						user.getUserName(), 
						user.getInitialCookies()
					);
			HttpSession session = request.getSession();
			session.setAttribute("userData", userData);
			return true;  
		}
	} 
	
	/* 
	 *  수강과목 정보 검색
	 * vueLoginCheck이후 로그인이 완료되어 있다면 실행
	 * 크롤링 id를 보내줌
	 */
	@RequestMapping("/getInitSubject")   
	public InitalPageData getInitData(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserData userData = (UserData)session.getAttribute("userData");
		
		InitalPageData initPageData = new InitalPageData();
		UserData user = (UserData)request.getSession().getAttribute("userData");
		String studentNumber = user.getStudentNumber();
		
		initPageData.setName(user.getUserName());
		initPageData.setStudentNumber(studentNumber);
		
		List<SubjectInfo> crawlingSubjects = eLearningService.crawlAndSaveBasicSubjectData(user); //크롤링 정보 가져오기
		List<UserSubject> dbUserSubject = vueService.searchUserSubject(studentNumber); //기존에 있던 과목정보
		vueService.saveUser(user); //User테이블에 정보가 없으면 저장
		
		//분기가 필요하다. 기존 db에 데이터가 있는 유저거나, db에 데이터가 없는 유저거나.
		if(dbUserSubject.isEmpty()) {
			//DB에 저장된 데이터가 전혀 없을때 전부 크롤링해서 db에 저장함
			eLearningService.saveBasicSubjectData(user, crawlingSubjects); //크롤링 정보 저장
		}
		else {
			List<SubjectInfo> needStoredSubjects = getNeedStoredSubjectData(dbUserSubject, crawlingSubjects);
			eLearningService.saveBasicSubjectData(user, needStoredSubjects); //필요한 사용자의 과목정보 정보 저장
		}
		
		initPageData.setSubjectCardData(vueService.getSubjectDTO(crawlingSubjects));
//		initPageData.setSubjectCardData(vueService.getInitCardData(studentNumber));
				
		return initPageData;
	}
	private List<SubjectInfo> getNeedStoredSubjectData(List<UserSubject> dbUserSubject,
			List<SubjectInfo> crawlingSubjects) {   
		List<SubjectInfo> needStoredSubjects = new ArrayList<SubjectInfo>();
		
		String[] dbSubjectId = new String[dbUserSubject.size()];
		String[] crawlingSubjectId = new String[crawlingSubjects.size()];
		
		int i = 0;
		for (UserSubject userSubject : dbUserSubject) {
			dbSubjectId[i] = userSubject.getSubjectId();
			i++;
		}
		
		i = 0;
		for (SubjectInfo subjectInfo : crawlingSubjects) {
			crawlingSubjectId[i] = subjectInfo.getSubjectId();
			i++;
		}
		
		for (int j = 0; j < crawlingSubjectId.length; j++) {
			if(Arrays.asList(dbSubjectId).contains(crawlingSubjectId[j])) {
				continue;
			}
			else { 
				needStoredSubjects.add(crawlingSubjects.get(j));
			}
		}
		
		if(needStoredSubjects.size() == 0) {
			return null;
		}
		else {
			System.out.println(needStoredSubjects.toString());
			return needStoredSubjects;
		}
	}

	/*
	@RequestMapping("/crawlSubject")
	public String crawlSubject(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		return "아이디: " + subjectId + ", 학번: " + user.getStudentNumber();
	}
	*/
	@RequestMapping("/crawlLecture")
	public List<LectureWeekData> crawlLecture(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<LectureWeekData> lectures = vueService.crawlLecture(user, subjectId);
		return lectures;
	}
	@RequestMapping("/crawlNotice")
	public List<NoticeData> crawlNotice(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<NoticeData> notice = vueService.crawlNotice(user, subjectId);
		return notice;
	}
	@RequestMapping("/crawlTask")
	public List<TaskData> crawlTask(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<TaskData> task = vueService.crawlTask(user, subjectId);  
		return task;
	}
	
	
	//강의 주차 검색
	@RequestMapping("/lectureDB")
	public List<LectureWeekData> lectureSearch(HttpServletRequest request, @RequestParam String subjectId) {
		System.out.println("과목 주차 조회");
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<LectureWeekData> lectureWeekList = vueService.getLectureeData(subjectId, user.getStudentNumber()); 
		return lectureWeekList;
	}
	
	//과제 검색 
	@RequestMapping("/taskDB")
	public List<TaskData> taskSearch(HttpServletRequest request, @RequestParam String subjectId) {
		System.out.println("과제 조회");
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<TaskData> taskList = vueService.getTaskData(subjectId, user.getStudentNumber());
		return taskList; 
	}
	
	//공지 검색
	@RequestMapping("/noticeDB")    
	public List<NoticeData> noticeSearch(@RequestParam String subjectId) {
		System.out.println("공지 조회");
		List<NoticeData> notices = vueService.getNoticeData(subjectId);
		if(notices == null) return null;
		
		return notices;
	}
	
}
