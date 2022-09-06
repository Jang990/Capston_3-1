package com.esummary.configuration.security.jwt;

import java.io.IOException;
import java.util.Date;

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
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		ObjectMapper om = new ObjectMapper();
		try {
			UserInfo user = om.readValue(request.getInputStream(), UserInfo.class);
			userRepository.findByStudentNumber(user.getStudentNumber());
			UsernamePasswordAuthenticationToken authenticationToken = 
					new UsernamePasswordAuthenticationToken(user.getStudentNumber(), user.getPassword());
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
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withClaim(JwtInfo.studentId, principalDetails.getUsername())
				.withClaim(JwtInfo.ELearningSessionID, principalDetails.getInhaTcSessionId())
				.withExpiresAt(JwtInfo.expireDate)
				.sign(Algorithm.HMAC256(JwtInfo.Key));
		
		response.addHeader("Authorization", "Bearer: "+ jwtToken);
	}
}
