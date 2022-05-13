package com.esummary.elearning.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil;
import com.esummary.elearning.service.test.TestService;

@Controller
public class TestController {
	@Autowired
	private TestService testService;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/test")
	public String login(HttpServletRequest request, Model model) { 
		UserInfo user = (UserInfo)request.getSession().getAttribute("userInfo");
		List<SubjectInfo> subjectList = testService.test(user);
//		return "testPage";
		
		model.addAttribute("name", user.getUserName());
		model.addAttribute("id", user.getStudentNumber());
		model.addAttribute("subjectList", subjectList);
		return "/mainApp/summary";
	}
	
	@RequestMapping("/testJPA")
	public String JPATest(HttpServletRequest request, Model model) { 
		UserInfo user = (UserInfo)request.getSession().getAttribute("userInfo");
		return "/test";
	}
	
	//Vue 페이지 테스트 
	@RequestMapping("/vue")
	public String vue() {
			return "/vue/index";
	}
	      
}
