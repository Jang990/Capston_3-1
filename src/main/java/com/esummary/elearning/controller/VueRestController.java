package com.esummary.elearning.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.test.TestService;
import com.esummary.elearning.service.vue.VueService;

@RestController
public class VueRestController {
	
	@Autowired
	private VueService vueService;
	
	@Autowired
	private LoginService eLearningLoginService;
	
	//로그인 체크
	@RequestMapping("/vueLoginCheck") 
	public boolean loginCheck(@RequestParam String id, @RequestParam String password, HttpServletRequest request) {
		UserInfo user = eLearningLoginService.login(id, password);
		if(user != null) {
			return false; 
		}   
		else {
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", user);
			return true; 
		}
	}
	
	//과제 검색 
	@RequestMapping("/taskDB")
	public Object taskSearch() {
		return null;
	}
	//공지 검색
	@RequestMapping("/noticeDB")
	public Object noticeSearch() {
		return null;
	}
	//강의검색
	@RequestMapping("/lectureDB")
	public Object lectureSearch() {
		return null;
	}
}
