package com.esummary.configuration.security.jwt.elearninglogin.usercheck;

import java.util.Map;

/**
 * 사용자의 개인정보를 가져오는 유틸. 이러닝의 로그인 쿠키(JSESSIONID)를 통해 로그인 성공 여부를 파악하기 위해 사용
 * @author Jang990
 *
 */
public interface ElearningUserCheckUtil {
	String getUserName(Map<String, String> loginCookies);
}
