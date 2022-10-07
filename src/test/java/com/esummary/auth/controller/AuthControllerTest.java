package com.esummary.auth.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.esummary.auth.dto.LoginDTO;

@SpringBootTest
class AuthControllerTest {

	@Autowired
	AuthController authController;
	
	@Test
	void testAuthorize() {
		String id = "201845096";
		String pw = "...";
		LoginDTO login = new LoginDTO(id, pw);
		authController.authorize(login);
	}

//	@Test
	void testSignup() {
	}

}
