package com.esummary.elearning.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.service.vue.VueService;

@Controller
public class HomePageController {
	@Autowired
	private VueService vueService;
	
	//Vue 페이지 테스트 
	@RequestMapping("/vue")
	public String vue(HttpServletRequest request, Model model) {
		  
		return "/vue/index";   
	}
	/*
	@RequestMapping("/vue")
	public String vue(HttpServletRequest request, Model model) {
		InitalPageData initPageData = new InitalPageData();
		UserInfo user = (UserInfo)request.getSession().getAttribute("userInfo");
		String name = user.getUserName();
		String studentNumber = user.getStudentNumber();
		
		//분기가 필요하다. 기존 db에 데이터가 있는 유저거나, db에 데이터가 없는 유저거나.
		initPageData.setName(name);
		initPageData.setStudentNumber(studentNumber);
		initPageData.setSubjectCardData(vueService.getInitCardData(studentNumber));
		
		model.addAttribute(initPageData); 
		
		return "/vue/index";
	}*/
}
