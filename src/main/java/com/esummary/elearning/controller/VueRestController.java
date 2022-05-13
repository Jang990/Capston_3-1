package com.esummary.elearning.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.service.test.TestService;

@RestController
public class VueRestController {
	@Autowired
	private TestService testService;
	
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	
	//Vue 페이지 테스트 
		@RequestMapping("/vue")
		public String vue() { 
			return "/vue/index";
	}
	
	//Vue DB연동 테스트 
	@RequestMapping("/vueDBu")
	public UserInfo vueDB() {
		//간단하게 user를 찾아서 반환해 봤다.
		UserInfo user = testService.getUserInfo("201845096");
		user.setInitialCookies(null);
		user.setSubjectList(null);
		user.setUserSubjects(null);//하나라도 null이 있다면 프론트엔드에서 받을 수 없다.
		return user; 
	}
	
	//Vue DB연동 테스트 
	@RequestMapping("/vueDB")
	public List<UserSubject> vueDBSearch() {
		//간단하게 user를 찾아서 반환해 봤다.
//		UserInfo user = testService.getUserInfo("201845096");
//		List<UserSubject> us = userSubjectRepository.findByStudentNumber(user.getStudentNumber());
//		System.out.println("텡:"+us);
//		return us; 
		return null;
	}
}
