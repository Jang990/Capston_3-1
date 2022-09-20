package com.esummary.elearning.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.exdto.user.UserToRegister;
import com.esummary.elearning.service.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	
	@PostMapping
	public boolean registerUser(@Valid UserToRegister user) {
		return userService.registerUser(user);
	}
}
