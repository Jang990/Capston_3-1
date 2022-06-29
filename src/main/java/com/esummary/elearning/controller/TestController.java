package com.esummary.elearning.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil;
import com.esummary.elearning.service.subject.util.db.DBSubjectUtil;
import com.esummary.elearning.service.test.TestService;

@RestController
public class TestController {
	@Autowired
	private TestService testService;
	
	@Autowired
	private DBSubjectUtil subjectUtil;
	
	@RequestMapping("/test")
	public String testController1(HttpServletRequest request) { 
		UserData user = (UserData)request.getSession().getAttribute("userData");
		System.out.println("앓");
		SubjectInfo sub = subjectUtil.getSubjectAllDetails("202214001LLA117");
		System.out.println("제어용: " + sub);
		return "연결확인";
	}    
	   
}
