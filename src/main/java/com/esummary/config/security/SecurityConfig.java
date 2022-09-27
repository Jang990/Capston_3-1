package com.esummary.config.security;

import org.springframework.context.annotation.Bean; 
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.esummary.config.security.jwt.JwtAccessDeniedHandler;
import com.esummary.config.security.jwt.JwtAuthenticationEntryPoint;
import com.esummary.config.security.jwt.JwtSecurityConfig;
import com.esummary.config.security.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig{
	
	private final CorsFilter corsFilter;
	private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf().disable()

                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling() //예외 핸들링부분
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console (h2 콘솔을 위한 설정)
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/api/hello").permitAll()
                //로그인, 회원가입은 토큰이 없는 상태에서 이뤄지기 때문에 다음과 같이 설정
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll() 

//                .anyRequest().authenticated()
                .anyRequest().permitAll() //프론트엔드 테스트를 위한 코드 - 나중에 수정할 것

                .and()
                .apply(new JwtSecurityConfig(tokenProvider)); // JwtFilter 적용

        return httpSecurity.build();
    }
	
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers(
        		"/static"
        		, "/resources/**"
//        		, "/h2-console/**"
//        		, "/favicon.ico"
//        		, "/error"
                , "/templates/**");
    }

	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
