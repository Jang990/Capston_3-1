package com.esummary.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
	@Value("${jwt.secret}")
	private final String secret;
	@Value("${jwt.token-validity-in-seconds}")
	private final long tokenValidityInMilliseconds;
	private Key key;

	public TokenProvider(@Value("${jwt.secret}") String secret,
			@Value("${jwt.token-validity-in-seconds}") long tokenSecond) {
		this.secret = secret;
		this.tokenValidityInMilliseconds = tokenSecond * 1000;
	}
	
	@PostConstruct
	public void init() {
		byte[] bytesKey = Decoders.BASE64.decode(secret);
		key = Keys.hmacShaKeyFor(bytesKey);
	}
	
	public String createToken(Authentication authentication) {
		// 권한을 String타입으로 변경
		String authorities = authentication.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
		
		//만료시간 설정
		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);
		
		return Jwts.builder()
				.setSubject(authentication.getName())
				.claim(AUTHORITIES_KEY, authorities)
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
		
		Collection<? extends GrantedAuthority> authorities = 
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		
		User principal = new User(claims.getSubject(), "", authorities);
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
