package com.esummary.elearning.controller;

import java.util.HashMap;
import java.util.List; 
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.service.crawling.user.UserCrawlingUtil;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.vue.VueService;

@RestController
public class LoginController {

	@Autowired
	private VueService vueService;
	
	@Autowired
	private LoginService eLearningLoginService;
	
	@Autowired
	private UserCrawlingUtil userCrawlingUtil;
	
	//로그인 체크
	@RequestMapping("/vueLoginCheck") 
	public boolean login(@RequestParam String id, @RequestParam String password, HttpServletRequest request) {
		//이러닝이 안되서 일단 테스트코드로 놓음 이러닝되면 지울 것
//		testCode(request);
//		return true;
		
//		/*
		Map<String, String> loginSessionCookie = eLearningLoginService.getLoginCookies(id, password);
		if(loginSessionCookie == null) // 로그인 실패 
			return false;
		
		//크롤링 
		String userName = userCrawlingUtil.getUserName(loginSessionCookie);
		UserData userData = new UserData(id, userName, loginSessionCookie);
		
		//db에 유저 정보 저장
		vueService.saveUserService(userData);
		
		//세션 설정
		HttpSession session = request.getSession();
		session.setAttribute("userData", userData);
		
		return true;
//		*/
	}

	private void testCode(HttpServletRequest request) {
		UserData userData = new UserData("201845096", "장현우", new HashMap<String, String>());
		HttpSession session = request.getSession();
		session.setAttribute("userData", userData);
	}
	
}
