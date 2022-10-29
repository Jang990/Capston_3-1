package com.esummary.subject.controller;

import java.util.List; 

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.crawling.dto.SubjectCountData;
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
@RequestMapping("/api/users/{studentId}/subject/{subjectId}")
@RequiredArgsConstructor
public class UserSpecificSubjectController {
	
	private final UserSubjectService userSubjectService;
	
	/**
	 * 과목에 해당하는 공지, 과제, 수업을 통합해서 리턴
	 * @param subjectId
	 * @return
	 */
	@GetMapping("/all")
	public SubjectDetailDataWithCnt_DTO getSubjectInfo(@PathVariable String studentId, @PathVariable String subjectId) {
		// 일단 시간 부족하니까 이렇게 만든다 - 원래는 쿼리를 한번만 보내도록 따로 로직처리를 하는게 좋다.
		List<LectureWeekData> lectureDTO = this.lectureSearch(studentId, subjectId);
		List<NoticeData> noticeDTO = this.noticeSearch(studentId, subjectId);
		List<TaskData> taskDTO = this.taskSearch(studentId, subjectId);
		SubjectCountData cntDTO = new SubjectCountData(lectureDTO, taskDTO);
		
		SubjectDetailDataWithCnt_DTO subjectDTO = new SubjectDetailDataWithCnt_DTO(lectureDTO, taskDTO, noticeDTO, cntDTO);
		return subjectDTO;
	}
	
	@GetMapping("/lecture")
	public List<LectureWeekData> lectureSearch(@PathVariable String studentId, @RequestParam String subjectId) {
		List<LectureWeekData> lectureWeekList = userSubjectService.getLectureData(studentId, subjectId); 
		return lectureWeekList;
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
