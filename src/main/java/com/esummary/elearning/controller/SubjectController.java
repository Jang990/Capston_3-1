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
import com.esummary.elearning.service.user.crawling.UserCrawlingUtil;
import com.esummary.elearning.service.vue.VueService;

@RestController
public class SubjectController {
	
	@Autowired
	private VueService vueService;
	
	//DB에서 가져오기
	@RequestMapping("/getSubjectInDB")
	public SubjectDetailData_VO subjectSearch(HttpServletRequest request, @RequestParam String subjectId) {
		List<LectureWeekData> lectureDTO = this.lectureSearch(request, subjectId);
		List<NoticeData> noticeDTO = this.noticeSearch(request, subjectId);
		List<TaskData> taskDTO = this.taskSearch(request, subjectId);
		SubjectCountData cntDTO = new SubjectCountData(lectureDTO, taskDTO);
		
		SubjectDetailData_VO subjectDTO = new SubjectDetailData_VO(lectureDTO, taskDTO, noticeDTO, cntDTO);
		return subjectDTO;
	}
	//강의 주차 검색
	@RequestMapping("/lectureDB")
	public List<LectureWeekData> lectureSearch(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<LectureWeekData> lectureWeekList = vueService.getLectureData(user, subjectId); 
		return lectureWeekList;
	}
	
	//과제 검색 
	@RequestMapping("/taskDB")
	public List<TaskData> taskSearch(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<TaskData> taskList = vueService.getTaskData(user, subjectId);
		return taskList;
	}
	
	//공지 검색
	@RequestMapping("/noticeDB")    
	public List<NoticeData> noticeSearch(HttpServletRequest request, @RequestParam String subjectId) {
		List<NoticeData> notices = vueService.getNoticeData(subjectId);
		if(notices == null) return null;
		
		return notices;
	}
	
}
