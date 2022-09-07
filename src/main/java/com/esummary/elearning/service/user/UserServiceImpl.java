package com.esummary.elearning.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	
	@Override
	public boolean registerUser(UserData user) {
		UserInfo userInfo = UserInfo.builder()
				.studentNumber(user.getStudentNumber())
				.nickname(user.getUserName())
				.build();
		userRepository.save(userInfo); 
		return true;
	}
	
	@Override
	public boolean checkIdDuplicate(String id) {
		//중복 확인
		return userRepository.findByStudentNumber(id).isPresent();
	}
	
	@Override
	public boolean checkNicknameDuplicate(String nickname) {
		//중복 확인
		return userRepository.findByStudentNumber(nickname).isPresent();
	}
}
