package com.esummary.subject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.auth.service.login.CustomUserDetails;
import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.tofront.SubjectCardDTO;
import com.esummary.subject.service.UserSubjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users/{studentId}")
@RequiredArgsConstructor
public class UserSubjectController {
	private final UserSubjectService userSubjectService;
	
	/** 
     * 현재 사용자가 가지고 있는 수업정보(subjectInfo에 수업명, 교수이름 등등) 가져오기<br> 
     * login-info 크롤링부분과 유사 
     */
    @GetMapping("/subject")
    public ResponseEntity<SubjectCardDTO> getUserOwnSubjectInfo(@AuthenticationPrincipal CustomUserDetails customUser) {
    	List<InhatcSubjectCardDTO> userOwnSubjectInfo = userSubjectService.getUserOwnSubjectInfo(customUser.getUsername());
        //TokenDto를 이용해 바디에도 넣어서 리턴 
        return new ResponseEntity<>(new SubjectCardDTO(userOwnSubjectInfo), HttpStatus.OK);
    }
}
