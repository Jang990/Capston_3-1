package com.esummary.subject.controller;

import java.util.List; 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.crawling.dto.tofront.SubjectDetailDataWithCnt_DTO;
import com.esummary.crawling.dto.tofront.TaskData;
import com.esummary.subject.service.UserSubjectService;

import lombok.RequiredArgsConstructor;

/**
 * 과목의 정보 + 사용자 정보(강의 시청시간, 과제 제출 여부 등)를 제공하는 컨트롤러
 * @author User
 *
 */
@RestController
@RequestMapping("api/users/{studentId}/subject/{subjectId}")
@RequiredArgsConstructor
public class UserSubjectController {
	
	private final UserSubjectService userSubjectService;
	
	/**
	 * 과목에 해당하는 공지, 과제, 수업을 통합해서 리턴
	 * @param subjectId
	 * @return
	 */
	@GetMapping("/all")
	public SubjectDetailDataWithCnt_DTO getSubjectInfo(@PathVariable String studentId, @PathVariable String subjectId) {
		return null;
	}
	
	@GetMapping("/lecture")
	public List<LectureWeekData> lectureSearch(@PathVariable String studentId, @RequestParam String subjectId) {
		return null;
	}
	
	@GetMapping("/task")
	public List<TaskData> taskSearch(@PathVariable String studentId, @RequestParam String subjectId) {
		userSubjectService.checkUserOwnSubject(studentId, subjectId);
		return userSubjectService.getTaskData(studentId, subjectId);
	}
	
	@GetMapping("/notice")
	public List<NoticeData> noticeSearch(@PathVariable String studentId, @RequestParam String subjectId) {
		userSubjectService.checkUserOwnSubject(studentId, subjectId);
		return userSubjectService.getNoticeData(subjectId);
	}
	
}
