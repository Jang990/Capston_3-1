package com.esummary.configuration.security.jwt;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.esummary.configuration.security.auth.PrincipalDetails;
import com.esummary.configuration.security.jwt.elearninglogin.ElearningLoginService;
import com.esummary.elearning.exdto.LoginCheck_DTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager;
	private final ElearningLoginService loginService;
	
	private Map<String, String> loginCookie; //이거 사용법을 찾아보자
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ObjectMapper om = new ObjectMapper();
		
		try {
			LoginCheck_DTO loginCheck = om.readValue(request.getInputStream(), LoginCheck_DTO.class);
			
			loginCookie = loginService.getLoginCookies(loginCheck);
			
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(loginCheck.getStudentNumber(), loginCheck.getPassword());
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		//이러닝이 안될때도 사용할 수 있도록 null도 가능하게 만들었다. 
		//때문에 회원가입부분에서 이러닝 계정과 아이디비번이 맞는지 빡세게 검사해야한다.
		String jsessionId = "";
		if(loginCookie != null) jsessionId = loginCookie.get("JSESSIONID"); 
		
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withClaim(JwtInfo.studentId, principalDetails.getUsername())
				.withClaim(JwtInfo.ELearningSessionID, jsessionId)
				.withExpiresAt(JwtInfo.expireDate)
				.sign(Algorithm.HMAC256(JwtInfo.Key));
		
		response.addHeader("Authorization", JwtInfo.prefix+ jwtToken);
	}
	
}
