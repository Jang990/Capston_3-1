package com.esummary.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtSecurirtyConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
	private TokenProvider tokenProvider;

	public JwtSecurirtyConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

	@Override
	public void configure(HttpSecurity http) {
		http.addFilterBefore(
				new JwtFilter(tokenProvider), 
				UsernamePasswordAuthenticationFilter.class
		);
	}
}