package com.esummary.crawling.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.auth.service.login.CustomUserDetails;
import com.esummary.crawling.dto.InhatcUserDTO;
import com.esummary.crawling.dto.TaskData;
import com.esummary.crawling.service.CrawlingService;
import com.esummary.crawling.service.InhatcCrawlingService;
import com.esummary.elearning.exdto.subject.LectureWeekData;
import com.esummary.elearning.exdto.subject.NoticeData;
import com.esummary.elearning.exdto.subject.SubjectCountData;
import com.esummary.elearning.exdto.subject.SubjectDetailDataWithCnt_DTO;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.elearning.exservice.vue.VueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inhatc/{subjectId}/subject")
@RequiredArgsConstructor
public class CrawlingSubjectController {
	@Autowired
	private VueService vueService;
	
	private final CrawlingService crawlingService;
	
	/**
	 * Subject와 관련된 모든 정보 크롤링
	 * @param request
	 * @param subjectId
	 * @return
	 */
	@GetMapping("/all")
	public SubjectDetailDataWithCnt_DTO crawlSubject(HttpServletRequest request, @PathVariable String subjectId) {
		//테스트 코드
//		return null;
		
//		/*
		List<LectureWeekData> lectureDTO = this.crawlLecture(request, subjectId);
		List<NoticeData> noticeDTO = this.crawlNotice(request, subjectId);
		List<TaskData> taskDTO = this.crawlTask(subjectId);
		SubjectCountData cntDTO = new SubjectCountData(lectureDTO, taskDTO);
		
		SubjectDetailDataWithCnt_DTO subjectVO = new SubjectDetailDataWithCnt_DTO(lectureDTO, taskDTO, noticeDTO, cntDTO);
		return subjectVO;
//		*/
	}
	
	@RequestMapping("/lecture")
	public List<LectureWeekData> crawlLecture(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<LectureWeekData> lectures = vueService.crawlLecture(user, subjectId);
		return lectures;
	}
	@RequestMapping("/notice")
	public List<NoticeData> crawlNotice(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<NoticeData> notice = vueService.crawlNotice(user, subjectId);
		return notice;
	}
	@PostMapping("/task")
	public List<TaskData> crawlTask(@AuthenticationPrincipal CustomUserDetails customUser, @PathVariable String subjectId) {
		InhatcUserDTO userDto = new InhatcUserDTO(customUser.getUsername(), customUser.getInhaTcSessionId());
		
		List<TaskData> task = crawlingService.crawlTask(userDto, subjectId);
		
		return task;
	}
}
