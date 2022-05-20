package com.esummary.elearning.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.subject.ELearningService;

@Controller
public class ELearningController {
	
	@Autowired
	private LoginService eLearningLoginService;
	@Autowired
	private ELearningService eLearningService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}        
	
	
	@RequestMapping("/login")
	public String login(Model model) { 
		
		/*  
			Document doc3;
			try {
				doc3 = Jsoup.connect("https://cyber.inhatc.ac.kr/Main.do?cmd=viewHome")
						.get();
				String RSAModulus = doc3.getElementById("RSAModulus").val();
				String RSAExponent = doc3.getElementById("RSAExponent").val();
				model.addAttribute("RSAModulus", RSAModulus);
				model.addAttribute("RSAExponent", RSAExponent);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		*/
		
		return "login";
	}
	
	@RequestMapping("/loginCheck")
	public String loginCheck(@RequestParam String id, @RequestParam String password, HttpServletRequest request) {
//		long beforeTime = System.currentTimeMillis();
//		long afterTime = System.currentTimeMillis(); 
//		long secDiffTime = (afterTime - beforeTime)/1000;
//		System.out.println("로그인시간(m) : "+secDiffTime);
		
		UserInfo user = eLearningLoginService.login(id, password);
		if(user == null) {
			return "redirect:/login";
		}
		else {
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", user);
//			return "redirect:/test"; // 단위 테스트용 url
			return "redirect:/vue"; // vue url
//			return "redirect:/mainApp/summary"; // 크롤링 데이터가 잘 오는지 확인 url 
		} 
	}
	
	@RequestMapping("/mainApp/summary")
	public String summary(HttpServletRequest request, Model model) {
		UserInfo user = (UserInfo)request.getSession().getAttribute("userInfo");
		
		List<SubjectInfo> subjectList = eLearningService.summary(user);
		
		model.addAttribute("name", user.getUserName());
		model.addAttribute("id", user.getStudentNumber());
		model.addAttribute("subjectList", subjectList);
		model.addAttribute("subject", subjectList.get(7));
		return "/mainApp/summary";
	}
	
}
