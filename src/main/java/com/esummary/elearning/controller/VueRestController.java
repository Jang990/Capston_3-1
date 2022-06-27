package com.esummary.elearning.controller;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
import com.esummary.elearning.dto.SubjectCountData;
import com.esummary.elearning.dto.SubjectDetailData_VO;
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
import com.esummary.elearning.service.user.crawling.UserCrawlingUtil;
import com.esummary.elearning.service.vue.VueService;

@RestController
public class VueRestController {
	
	@Autowired
	private VueService vueService;
	
	@Autowired
	private LoginService eLearningLoginService;
	@Autowired
	private UserCrawlingUtil userCrawlingUtil;
	
	
	//로그인 체크
	@RequestMapping("/vueLoginCheck") 
	public boolean login(@RequestParam String id, @RequestParam String password, HttpServletRequest request) {
		Map<String, String> loginSessionCookie = eLearningLoginService.getLoginCookies(id, password);
		if(loginSessionCookie == null) // 로그인 실패
			return false;
		
		//크롤링 
		String userName = userCrawlingUtil.getUserName(loginSessionCookie);
		UserData userData = new UserData(id, userName, loginSessionCookie);
		
		//db에 유저 정보 저장
		vueService.saveUserService(userData);
		
		//세션 설정
		HttpSession session = request.getSession();
		session.setAttribute("userData", userData);
		return true;  
	} 
	
	/* 
	 *  수강과목 정보 검색
	 * vueLoginCheck이후 로그인이 완료되어 있다면 실행
	 * 1. 크롤링으로 정보 가져옴
	 * 2. 크롤링정보(si)랑 사용자정보(us) 저장.
	 */
	@RequestMapping("/getInitSubject")   
	public InitalPageData getInitData(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserData user = (UserData)session.getAttribute("userData");
		InitalPageData initPageData = vueService.crawlInitDataService(user);
//		testCode(initPageData.getSubjectCardData());
		return initPageData;
	}
	
	private void testCode(List<SubjectCardData> subjectCardData) {
		/*
		만약 이러닝에 1학기 정보가 내려갔을때 테스트를 위한 코드
		SubjectCardData(subjectId=CORS_1703071437557d610794, subjectName=서버프로그래밍, owner=조규철), 
		SubjectCardData(subjectId=CORS_1704050920510f7c44c0, subjectName=프로그래밍, owner=관리자), 
		SubjectCardData(subjectId=202214001LLA117, subjectName=[1학년L반] 발명과특허, owner=김영준), 
		SubjectCardData(subjectId=202214001LLA138, subjectName=[1학년L반] 빅데이터의이해, owner=용승림), 
		SubjectCardData(subjectId=202211141LLA104, subjectName=[1학년L반] 자기개발과직업윤리, owner=김성진), 
		SubjectCardData(subjectId=202214043CMP389, subjectName=[3학년C반] 빅데이터, owner=권두순), 
		SubjectCardData(subjectId=202214043C4846, subjectName=[3학년C반] 소프트웨어분석설계, owner=김철진), 
		SubjectCardData(subjectId=202214043C5019, subjectName=[3학년C반] 웹보안, owner=용승림), 
		SubjectCardData(subjectId=202214043CMP743, subjectName=[3학년C반] 자율드론, owner=박병섭), 
		SubjectCardData(subjectId=202214043DMP250, subjectName=[3학년D반] 캡스톤디자인, owner=황수철)])
		*/
		subjectCardData.add(new SubjectCardData("202214001LLA117", "[1학년L반] 발명과특허", "김영준"));
		subjectCardData.add(new SubjectCardData("202214001LLA138", "[1학년L반] 빅데이터의이해", "용승림"));
		subjectCardData.add(new SubjectCardData("202211141LLA104", "[1학년L반] 자기개발과직업윤리", "김성진"));
		subjectCardData.add(new SubjectCardData("202214043CMP389", "[3학년C반] 빅데이터", "권두순"));
		subjectCardData.add(new SubjectCardData("202214043C4846", "[3학년C반] 소프트웨어분석설계", "김철진"));
		subjectCardData.add(new SubjectCardData("202214043C5019", "[3학년C반] 웹보안", "용승림"));
		subjectCardData.add(new SubjectCardData("202214043CMP743", "[3학년C반] 자율드론", "박병섭"));
		subjectCardData.add(new SubjectCardData("202214043DMP250", "[3학년D반] 캡스톤디자인", "황수철"));
	}

	@RequestMapping("/crawlSubject")
	public SubjectDetailData_VO crawlSubject(HttpServletRequest request, @RequestParam String subjectId) {
		List<LectureWeekData> lectureDTO = this.crawlLecture(request, subjectId); //여기 리팩토링중
		List<NoticeData> noticeDTO = this.crawlNotice(request, subjectId);
		List<TaskData> taskDTO = this.crawlTask(request, subjectId);
		SubjectCountData cntDTO = new SubjectCountData(lectureDTO, taskDTO);
		
		SubjectDetailData_VO subjectVO = new SubjectDetailData_VO(lectureDTO, taskDTO, noticeDTO, cntDTO);
		return subjectVO;   
	}
	//여기하는중
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
	
	
	//DB에서 가져오기
	@RequestMapping("/getSubjectInDB")
	public SubjectDetailData_VO subjectSearch(HttpServletRequest request, @RequestParam String subjectId) {
		System.out.println("db에서 조회합니다.");
		List<LectureWeekData> lectureDTO = this.lectureSearch(request, subjectId);
		List<NoticeData> noticeDTO = this.noticeSearch(request, subjectId);
		List<TaskData> taskDTO = this.taskSearch(request, subjectId);
		SubjectCountData cntDTO = new SubjectCountData(lectureDTO, taskDTO);
		
		SubjectDetailData_VO subjectDTO = new SubjectDetailData_VO(lectureDTO, taskDTO, noticeDTO, cntDTO);
		System.out.println("이거 맞음?:"+subjectDTO);
		return subjectDTO;
	}
	//강의 주차 검색
	@RequestMapping("/lectureDB")
	public List<LectureWeekData> lectureSearch(HttpServletRequest request, @RequestParam String subjectId) {
		System.out.println("과목 주차 조회");
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<LectureWeekData> lectureWeekList = vueService.getLectureData(subjectId, user.getStudentNumber()); 
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
	public List<NoticeData> noticeSearch(HttpServletRequest request, @RequestParam String subjectId) {
		System.out.println("공지 조회");
		List<NoticeData> notices = vueService.getNoticeData(subjectId);
		if(notices == null) return null;
		
		return notices;
	}
	
}
