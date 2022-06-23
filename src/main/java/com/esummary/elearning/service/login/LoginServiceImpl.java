package com.esummary.elearning.service.login;
 
import java.io.IOException;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements LoginService {
	public final String MAIN_URL = "https://cyber.inhatc.ac.kr";
	
	@Override
	public Map<String, String> getLoginCookies(String id, String password) {
		Map<String, String> initialCookies = this.getInitialCookies("/MMain.do?cmd=viewIndexPage"); //초기 쿠키 가져오기
		if(this.checkLogin(id, password, initialCookies)) {
			return initialCookies; //로그인 세션 쿠키 반환
		}
		
		return null; // 로그인 실패.
	}
	
	private boolean checkLogin(String id, String password, Map<String, String> initialCookies) {
		final String LOGIN_URL = MAIN_URL + "/MUser.do";
		Connection con = Jsoup.connect(LOGIN_URL)
				.data("cmd", "loginUser")
				.data("userDTO.userId", id)
				.data("userDTO.password", password)
				.data("userDTO.localeKey", "ko")
				.cookies(initialCookies);
		
		try {
			Connection.Response resp = con.execute();
			if(resp.statusCode() == 200) {
//				setUserInfo(initialCookies);
//				userRepository.save(user);		//DB에 저장
				con.post();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false; 
	}

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
	
	
	
}
