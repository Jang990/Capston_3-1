package com.esummary.auth.service;

import java.util.Map;

import com.esummary.auth.dto.LoginDTO;

/**
 * 로그인 시 이러닝 로그인 쿠키(JSESSIONID)를 JWT 토큰에 담기위한 기능을 가지고 있는 유틸
 * 이러닝의 로그인 쿠키(JSESSIONID)를 통해 로그인 성공 여부를 파악하기 위해 사용
 * @author Jang990
 *
 */
public interface ElearningLoginService {
	
	Map<String, String> getLoginCookies(LoginDTO loginCheck);
}
