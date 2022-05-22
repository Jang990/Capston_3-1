package com.esummary.elearning.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.dto.InitalPageData;
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
	
	//수강과목 정보 검색 vueLoginCheck이후 로그인이 완료되어 있다면 실행
	@RequestMapping("/getInitSubject")   
	public InitalPageData getInitData(HttpServletRequest request) {
		InitalPageData initPageData = new InitalPageData();
		UserData user = (UserData)request.getSession().getAttribute("userData");
		String name = user.getUserName();
		String studentNumber = user.getStudentNumber();
		
		//분기가 필요하다. 기존 db에 데이터가 있는 유저거나, db에 데이터가 없는 유저거나.
		if(vueService.isExistUserSubjectInDB(studentNumber)) {
			//가져오기.
			initPageData.setName(name);
			initPageData.setStudentNumber(studentNumber);		
			initPageData.setSubjectCardData(vueService.getInitCardData(studentNumber));
			return initPageData; 
		}
		else {
			//크롤링.
			System.out.println("유저체크 크롤링");
			return null;
		}
		
		
		
	}
	
	//
	@RequestMapping("/mainApp/summaryTest")
	public String summary(HttpServletRequest request, Model model) {
		UserInfo user = (UserInfo)request.getSession().getAttribute("userInfo");
		
		List<SubjectInfo> subjectList = eLearningService.summary(user);
		
		model.addAttribute("name", user.getUserName());
		model.addAttribute("id", user.getStudentNumber());
		model.addAttribute("subjectList", subjectList);
		model.addAttribute("subject", subjectList.get(7));
		return "/mainApp/summary";
	}
	
	//강의 주차 검색
	@RequestMapping("/lectureDB")
	public List<LectureWeekData> lectureSearch(@RequestParam String subjectId, @RequestParam String studentNumber) {
		System.out.println("과목 주차 조회");
		List<LectureWeekData> lectureWeekList = vueService.getLectureeData(subjectId, studentNumber); 
		return lectureWeekList;
	}
	
	//과제 검색 
	@RequestMapping("/taskDB")
	public List<TaskData> taskSearch(@RequestParam String subjectId, @RequestParam String studentNumber) {
		System.out.println("과제 조회"); 
		List<TaskData> taskList = vueService.getTaskData(subjectId, studentNumber);
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
