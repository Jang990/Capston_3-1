package com.esummary.elearning.service.login.util.login;

import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;

@Component
public class LoginUtil_Inhatc implements LoginUtil{
	public final String MAIN_URL = "https://cyber.inhatc.ac.kr";
	
	@Autowired
	private UserRepository userRepository;

	public Map<String, String> getInitialCookies(String loginParameterUrl) {
		final String MAIN_PAGE_URL = MAIN_URL + loginParameterUrl;
		
		try {
			Connection.Response initial = Jsoup.connect(MAIN_PAGE_URL)
					.method(Connection.Method.GET)
					.execute();
			Map<String, String> initialCookies = initial.cookies();
			return initialCookies;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean loginToUsingUserData(String id, String password, UserInfo user){
		final String LOGIN_URL = ELearningServiceImpl.MAIN_URL + "/MUser.do";
		
		try {
			Connection con = Jsoup.connect(LOGIN_URL)
					.data("cmd", "loginUser")
					.data("userDTO.userId", id)
					.data("userDTO.password", password)
					.data("userDTO.localeKey", "ko")
					.cookies(user.getInitialCookies());
			Connection.Response resp = con.execute();
			
			if(resp.statusCode() == 200) {
				con.post();
				setUserInfo(user);
				userRepository.save(user);		//DB에 저장
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private UserInfo setUserInfo(UserInfo user) {
		Document loginPage = conLoginPage(user.getInitialCookies());
		if(loginPage == null) return null;
		System.out.println(loginPage);
		String[] nameAndWStudentNumber = loginPage.getElementsByClass("login_info").select("ul li").last().text().split(" ");
		String name = nameAndWStudentNumber[0].trim();
		String id = nameAndWStudentNumber[1].trim().replace("(", "").replace(")", "");
		
		user.setUserName(name);
		user.setStudentNumber(id);
		return user;
	}

	public Document conLoginPage(Map<String, String> initialCookies) {
		String mainUrl = ELearningServiceImpl.MAIN_URL + "/MMain.do";
		Document loginPage = null;
		
		try {
			Connection con = Jsoup.connect(mainUrl)
					.data("cmd", "viewIndexPage")
					.cookies(initialCookies);
			Connection.Response resp = con.execute();
			
			if(resp.statusCode() == 200)
				loginPage = con.post();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loginPage;
	}
	
}
