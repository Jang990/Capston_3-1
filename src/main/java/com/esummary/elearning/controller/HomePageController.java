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
	public String vue() {
		return "/vue/index";   
	} 

}
