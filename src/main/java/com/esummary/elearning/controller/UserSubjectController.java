package com.esummary.elearning.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.auth.service.User.UserService;
import com.esummary.elearning.dto.subject.SubjectDTO;
import com.esummary.subject.service.UserSubjectService;

import lombok.RequiredArgsConstructor;


/**
 * 이러닝에 과목 정보들을 사용자 정보(과제-제출정보, 강의-시청시간 등)을 포함해서 가져오는 컨트롤러
 * @author User
 */
@RestController
@RequestMapping("/users/{userId}/subject/{subjectId}")
@RequiredArgsConstructor
public class UserSubjectController {
	
//	private final UserSubjectService userSubjectService;
	
	@GetMapping("")
	public List<SubjectDTO> getUserOwnSubject(@PathVariable String userId, @PathVariable String subjectId) {
		
		return null;
	}
	
}
