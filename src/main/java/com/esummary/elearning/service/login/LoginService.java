package com.esummary.elearning.service.login;

import java.util.Map;


public interface LoginService {
	//로그인을 시도하고 로그인에 성공하면 로그인한 세션 쿠키를 가져옴.
	Map<String, String> getLoginCookies(String id, String password);
}
