package com.esummary.configuration.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.esummary.configuration.security.jwt.JwtAuthenticationFilter;
import com.esummary.configuration.security.jwt.JwtAuthorizationFilter;
import com.esummary.elearning.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsConfig;
	private final UserRepository userRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests()
//			.antMatchers("/api/user").hasRole("USER")
			.anyRequest().permitAll();

		http
			.csrf().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.addFilter(corsConfig)
			.addFilter(new JwtAuthenticationFilter(authenticationManager(), userRepository))
			.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository));
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
