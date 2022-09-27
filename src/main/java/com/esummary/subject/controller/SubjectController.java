package com.esummary.subject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.crawling.dto.SubjectDetailDataWithCnt_DTO;
import com.esummary.elearning.dto.subject.SubjectDTO;
import com.esummary.subject.service.SubjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {

	private final SubjectService subjectService;
	
	/**
	 * SubjectInfo 테이블에서 단순 subject에 대한 정보 불러오기
	 * @param subjectId
	 * @return SubjectInfo 테이블 정보를 그대로 줌
	 */
	@GetMapping("/{subjectId}")
	public SubjectDTO getSubjectInfo(@PathVariable String subjectId) {
		return subjectService.getSubject(subjectId);
	}
	
	
}
