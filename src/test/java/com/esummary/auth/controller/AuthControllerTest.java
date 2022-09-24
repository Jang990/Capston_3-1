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
		LoginDTO login = new LoginDTO("201845096", "!gusdn0908");
		authController.authorize(login);
	}

//	@Test
	void testSignup() {
	}

}
