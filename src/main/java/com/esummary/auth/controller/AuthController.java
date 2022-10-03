package com.esummary.auth.controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.auth.dto.JwtTokenDTO;
import com.esummary.auth.dto.LoginDTO;
import com.esummary.auth.dto.SignUpUserDTO;
import com.esummary.auth.service.ElearningLoginService;
import com.esummary.auth.service.User.SignUpService;
import com.esummary.auth.service.login.CustomUserDetails;
import com.esummary.config.security.jwt.JwtFilter;
import com.esummary.config.security.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import javax.validation.Valid;


/**
 * 로그인 및 회원가입을 위한 컨트롤러
 * @author User
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SignUpService userService;
    private final ElearningLoginService loginService;

    // 로그인 API
    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenDTO> authorize(@Valid @RequestBody LoginDTO loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getStudentId(), loginDto.getPassword());
        
        //Authentication 객체 생성 -> CustomUserDetailsService에 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken); 
        
        // 이러닝 로그인 쿠키 세팅
        Map<String, String> loginCookie = loginService.getLoginCookies(loginDto);
        ((CustomUserDetails)authentication.getPrincipal()).setInhaTcSessionId(loginCookie);
        
        //SecurityContextHolder에 담기
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        
        //생성한 객체를 가지고 JWT Token 생성
        String jwt = tokenProvider.createToken(authentication);

        //헤더에 JWT token을 넣고
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        
        //TokenDto를 이용해 바디에도 넣어서 리턴 
        return new ResponseEntity<>(new JwtTokenDTO(jwt), httpHeaders, HttpStatus.OK);
    }
    
    //회원가입
    @PostMapping("/signup")
	public ResponseEntity<SignUpUserDTO> signup(@Valid @RequestBody SignUpUserDTO signUpUserDTO) {
		return ResponseEntity.ok(userService.signup(signUpUserDTO));
	}
    
}