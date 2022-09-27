package com.esummary.crawling.controller;

import java.util.List;  

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.auth.service.login.CustomUserDetails;
import com.esummary.crawling.dto.InhatcUserDTO;
import com.esummary.crawling.dto.LectureWeekData;
import com.esummary.crawling.dto.NoticeData;
import com.esummary.crawling.dto.SubjectCountData;
import com.esummary.crawling.dto.SubjectDetailDataWithCnt_DTO;
import com.esummary.crawling.dto.TaskData;
import com.esummary.crawling.service.CrawlingService;

import lombok.RequiredArgsConstructor;

/**
 * 인하공전 이러닝에서 강의와 관련된 것들을 크롤링
 * 크롤링한 정보들을 DB에 저장하고 반환
 * @author User
 *
 */
@RestController
@RequestMapping("/api/inhatc/subject/{subjectId}/")
@RequiredArgsConstructor
public class CrawlingSubjectController {
	
	private final CrawlingService crawlingService;
	
	@PostMapping("/all")
	public SubjectDetailDataWithCnt_DTO crawlSubject(@AuthenticationPrincipal CustomUserDetails customUser, @PathVariable String subjectId) {
		List<LectureWeekData> lectureDTO = this.crawlLecture(customUser, subjectId);
		List<NoticeData> noticeDTO = this.crawlNotice(customUser, subjectId);
		List<TaskData> taskDTO = this.crawlTask(customUser, subjectId);
		SubjectCountData cntDTO = new SubjectCountData(lectureDTO, taskDTO);
		
		SubjectDetailDataWithCnt_DTO subjectDTO = new SubjectDetailDataWithCnt_DTO(lectureDTO, taskDTO, noticeDTO, cntDTO);
		return subjectDTO;
	}
	
	@RequestMapping("/lecture")
	public List<LectureWeekData> crawlLecture(@AuthenticationPrincipal CustomUserDetails customUser, @PathVariable String subjectId) {
		InhatcUserDTO userDto = new InhatcUserDTO(customUser.getUsername(), customUser.getInhaTcSessionId());
		List<LectureWeekData> lectures = crawlingService.crawlLecture(userDto, subjectId);
		return lectures;
	}
	@PostMapping("/notice")
	public List<NoticeData> crawlNotice(@AuthenticationPrincipal CustomUserDetails customUser, @PathVariable String subjectId) {
		InhatcUserDTO userDto = new InhatcUserDTO(customUser.getUsername(), customUser.getInhaTcSessionId());
		List<NoticeData> notice = crawlingService.crawlNotice(userDto, subjectId);
		return notice;
	}
	@PostMapping("/task")
	public List<TaskData> crawlTask(@AuthenticationPrincipal CustomUserDetails customUser, @PathVariable String subjectId) {
		InhatcUserDTO userDto = new InhatcUserDTO(customUser.getUsername(), customUser.getInhaTcSessionId());
		List<TaskData> task = crawlingService.crawlTask(userDto, subjectId);
		return task;
	}
}
