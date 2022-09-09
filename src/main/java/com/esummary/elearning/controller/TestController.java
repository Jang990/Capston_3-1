package com.esummary.elearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	@RequestMapping("/reg")
	public String registerPage() {
		return "register";
	}
	
	@RequestMapping("/userCheck")
	public String ucPage() {
		return "forUserPage";
	}
	
	@RequestMapping("/lg")
	public String lgPage() {
		return "login";
	}
}
