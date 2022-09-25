package com.esummary.auth.service;
 
import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import com.esummary.auth.dto.LoginDTO;

import lombok.RequiredArgsConstructor;

@Service
public class LoginServiceImpl_Inhatc implements ElearningLoginService {
	public final String MAIN_URL = "https://cyber.inhatc.ac.kr";
	
	/*
	 * 개선 여지
	 * throw Exception을 해서 (로그인페이지 or 이름)을 찾을 수 없을 때 에러를 던져서 처리하도록 만들기
	 */
	
	@Override
	public Map<String, String> getLoginCookies(LoginDTO loginCheck) {
		Map<String, String> initialCookies = this.getInitialCookies("/MMain.do?cmd=viewIndexPage"); //초기 쿠키 가져오기
		if(this.attemptToLogin(loginCheck.getStudentId(), loginCheck.getPassword(), initialCookies)) {
			return initialCookies; //로그인 세션 쿠키 반환
		}
		
		return null; // 로그인 실패.
	}
	
	/**
	 * Inhatc 이러닝 사이트에 로그인 시도
	 * @param id
	 * @param password
	 * @param initialCookies
	 * @return
	 */
	private boolean attemptToLogin(String id, String password, Map<String, String> initialCookies) {
		final String LOGIN_URL = MAIN_URL + "/MUser.do";
		Connection con = Jsoup.connect(LOGIN_URL)
				.data("cmd", "loginUser")
				.data("userDTO.userId", id)
				.data("userDTO.password", password)
				.data("userDTO.localeKey", "ko")
				.cookies(initialCookies);
		
		try {
			Connection.Response resp = con.execute();
			if(resp.statusCode() != 200)
				return false;
			
			con.post();
			if(this.getUserName(initialCookies) == null) 
				return false; //로그인 쿠키를 사용했을 때 페이지에서 개인정보란을 찾을 수 없음
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false; 
	}

	/**
	 * 로그인을 시도할 사이트의 초기 쿠키를 가져온다.
	 * @param loginParameterUrl
	 * @return
	 */
	private Map<String, String> getInitialCookies(String loginParameterUrl) {
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
	
	
	/**
	 * 로그인에 성공해서 사용자의 이름을 가져올 수 있는지 확인하기 위한 메소드
	 * @param loginCookies
	 * @return
	 */
	private String getUserName(Map<String, String> loginCookies) {
		Document loginPage = conLoginPage(loginCookies);
		if(loginPage == null) return null;
		String name = crawlName(loginPage);
		return name;
	}
	
	/**
	 * 로그인 페이지에서 사용자의 실제 이름을 가져온다.
	 * @param loginPage
	 * @return 사용자의 이름을 가져올 수 없으면 null 리턴
	 */
	private String crawlName(Document loginPage) {
		Element str = loginPage.getElementsByClass("login_info").select("ul li").last(); //정보를 찾을 수 없음. 즉 로그인이 되지 않은 쿠키라는 것(또는 만료된 로그인 쿠키라는 것)
		if(str == null) 
			return null;
		
		String[] nameAndWStudentNumber = str.text().split(" ");
		return nameAndWStudentNumber[0].trim();
	}
	
	/**
	 * 로그인 시도할 페이지 연결
	 * @param attemptedLoginCookies
	 * @return
	 */
	private Document conLoginPage(Map<String, String> attemptedLoginCookies) {
		String mainUrl = MAIN_URL + "/MMain.do";
		Document loginPage = null;
		
		try {
			Connection con = Jsoup.connect(mainUrl)
					.data("cmd", "viewIndexPage")
					.cookies(attemptedLoginCookies);
			Connection.Response resp = con.execute();
			
			if(resp.statusCode() == 200)
				loginPage = con.post();
			else
				return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loginPage;
	}
	
}
