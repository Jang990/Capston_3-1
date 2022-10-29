package com.esummary.config.security.jwt;

import java.security.Key; 
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.esummary.auth.exception.DeniedElearningCookieException;
import com.esummary.auth.service.ElearningLoginService;
import com.esummary.auth.service.login.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {
	
	private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
	private static final String AUTHORITIES_KEY = "auth";
	private static final String ELEARNING_SESSION_ID = "EId";
	
	private final String secret;
	private final long tokenValidityInMilliseconds;
	private Key key;

	public TokenProvider(@Value("${jwt.secret}") String secret,
			@Value("${jwt.token-validity-in-seconds}") long tokenSecond,
			ElearningLoginService loginService) {
		this.secret = secret;
		this.tokenValidityInMilliseconds = tokenSecond * 1000;
	}
	
	@PostConstruct
	public void init() {
		byte[] bytesKey = Decoders.BASE64.decode(secret);
		key = Keys.hmacShaKeyFor(bytesKey);
	}
	
	public String createToken(Authentication authentication) {
		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
		
		// 권한을 String타입으로 변경
		String authorities = user.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
		
		//만료시간 설정
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);
		
		Map<String, String> inhatcSID;
		try {
			inhatcSID = user.getInhaTcSessionId();
		}
		catch (DeniedElearningCookieException e) {
			// 회원가입시에만 빡빡하게 검사 
			// 로그인 시에는 인하공전 이러닝 사이트가 맛이가도 로그인이 되어야 한다.
			inhatcSID = new HashMap<String, String>();
			inhatcSID.put("JSESSIONID", "");
		}
		
		return Jwts.builder()
				.setSubject(user.getUsername())
				.claim(AUTHORITIES_KEY, authorities)
				.claim(ELEARNING_SESSION_ID, inhatcSID) //이러닝 로그인 세션 삽입
				.signWith(key, SignatureAlgorithm.HS512)
				.setExpiration(validity)
				.compact();
	}
	
	public Authentication getAuthentication(String token) {
		/*
		 * 만약 토큰이 있다면 토큰에 해당하는 세션을 세팅해주어야 함
		 * 그래서 Authentication객체를 만들어고 필터에서 사용하도록 넘김
		 */
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		
		String authString = claims.get(AUTHORITIES_KEY).toString();
		Map<String, String> loginCookies = (Map<String, String>) claims.get(ELEARNING_SESSION_ID);
		
		Collection<? extends GrantedAuthority> authorities = 
				Arrays.stream(authString.split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		CustomUserDetails principal = new CustomUserDetails(claims.getSubject(), "", authString, loginCookies);
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			logger.info("잘못된 JWT 서명입니다.");
		} catch (ExpiredJwtException e) {
			logger.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			logger.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalArgumentException e) {
			logger.info("JWT 토큰이 잘못되었습니다.");
		}
		
		return false;
	}
}
