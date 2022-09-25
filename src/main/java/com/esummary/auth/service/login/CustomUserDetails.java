package com.esummary.auth.service.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.esummary.auth.entity.Authority;
import com.esummary.elearning.entity.user.UserInfo;

public class CustomUserDetails implements UserDetails {
	private UserInfo user;
	private Map<String, String> loginCookie;
	
	public CustomUserDetails(UserInfo user) {
		this.user = user;
		loginCookie = null;
	}

	public CustomUserDetails(String studentId, String password, String authString,
			Map<String, String> loginCookies) {
		this.user = UserInfo.builder()
				.studentNumber(studentId)
				.password(password)
				.roles(Authority.valueOf(authString))
				.build();
		this.loginCookie = loginCookies;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRoles().name()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getStudentNumber();
	}
	
	public Map<String, String> getInhaTcSessionId() {
		return this.loginCookie;
	}
	public void setInhaTcSessionId(Map<String, String> loginCookie) {
		this.loginCookie = loginCookie;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
