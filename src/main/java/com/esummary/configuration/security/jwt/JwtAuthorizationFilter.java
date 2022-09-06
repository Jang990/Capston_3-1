package com.esummary.configuration.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.esummary.configuration.security.auth.PrincipalDetails;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter{
	
	private final UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String jwtHeader = request.getHeader("Authorization");
		
		//헤더 확인
		if(jwtHeader == null || !jwtHeader.startsWith(JwtInfo.prefix)) {
			chain.doFilter(request, response);
			return;
		}
		
		//토큰 뜯어보기
		String jwtToken = request.getHeader("Authorization").replace(JwtInfo.prefix, "");
		String studentId = JWT.require(Algorithm.HMAC512(JwtInfo.Key)).build()
				.verify(jwtToken)
				.getClaim(JwtInfo.studentId).asString();
		
		//토큰내용이 없음
		if(studentId == null) {
			chain.doFilter(request, response);
			return;
		}
		
		Optional<UserInfo> userCheck = userRepository.findByStudentNumber(studentId);
		//DB에 없음
		if(userCheck.isEmpty()) {
			chain.doFilter(request, response);
			return;
		}
		
		//정상처리 - 인증된 사용자이기 때문에 임의로 Authentication 객체를 만들어서 세션에 넣음
		PrincipalDetails user = new PrincipalDetails(userCheck.get());
		Authentication authentication = 
				new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}
}
