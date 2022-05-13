package com.esummary.elearning.service.login;
 
import java.util.Map; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.service.login.util.login.LoginUtil;

@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private LoginUtil loginUtil;
	
	@Override
	public UserInfo login(String id, String password) {
		UserInfo user = new UserInfo();
		
		Map<String, String> initialCookies = loginUtil.getInitialCookies("/MMain.do?cmd=viewIndexPage"); //초기 쿠키 가져오기
		user.setInitialCookies(initialCookies);
		if(loginUtil.loginToUsingUserData(id, password, user)) //쿠기와 id, pw를 이용하여 이러닝 로그인 시도
			return user;
		else
			return null;
		
	}
	
}
