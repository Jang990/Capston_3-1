package com.esummary.auth.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.esummary.auth.dto.LoginDTO;
import com.esummary.auth.entity.Authority;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserInfo;
import com.esummary.entity.user.UserSubject;
import com.esummary.repository.user.UserRepository;

import lombok.Builder;

@SpringBootTest
class AuthControllerTest {

	@Autowired
	AuthController authController;
	
	@Autowired
	BCryptPasswordEncoder password;
	
//	@Test
	void testAuthorize() {
//		String id = "201845096";
//		String pw = "...";
//		LoginDTO login = new LoginDTO(id, pw);
//		authController.authorize(login);
	}
	
//	@Test
	void test() {
		System.out.println(password.encode("testtest"));
	}

}
