package com.esummary.configuration.security.jwt.elearninglogin;

import java.util.Map;

import com.esummary.elearning.exdto.LoginCheck_DTO;

/**
 * 로그인 시 이러닝 로그인 쿠키(JSESSIONID)를 JWT 토큰에 담기위한 기능을 가지고 있는 유틸
 * @author Jang990
 *
 */
public interface ElearningLoginService {
	Map<String, String> getLoginCookies(LoginCheck_DTO loginCheck);
}
