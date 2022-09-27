package com.esummary.elearning.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Vue로 만든 메인페이지를 보여주기 위한 컨트롤러
 * @author User
 *
 */
@Controller
public class MainPageController {
	
	@GetMapping({"", "/"})
	public String mainPage() {
		return "vue/index";
	}
}
