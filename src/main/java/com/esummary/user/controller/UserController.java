package com.esummary.user.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.auth.service.login.CustomUserDetails;
import com.esummary.user.dto.UserDTO;
import com.esummary.user.service.UserService;

import lombok.RequiredArgsConstructor;



/**
 * 사용자 정보 가져오기
 * @author User
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	
    // 현재 사용자의 기본 정보를(학번, 닉네임, 가입일) 반환 
    @GetMapping("")
    public ResponseEntity<UserDTO> getUserData(@AuthenticationPrincipal CustomUserDetails customUser) {
        //TokenDto를 이용해 바디에도 넣어서 리턴 
        return new ResponseEntity<>(userService.getUserData(customUser.getUsername()), HttpStatus.OK);
    }
    
}