package com.esummary.elearning.service.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.elearning.exdto.user.UserToRegister;
import com.esummary.elearning.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//예전 메서드
	@Override
	public boolean exRegisterUser(UserData user) {
		UserInfo userInfo = UserInfo.builder()
				.studentNumber(user.getStudentNumber())
				.nickname(user.getUserName())
				.build();
		userRepository.save(userInfo); 
		return true;
	}
	
	@Override
	public boolean registerUser(UserToRegister user) {
		checkIdDuplicate(user.getStudentId());
		checkNicknameDuplicate(user.getNickname());
		
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		UserInfo userInfo = UserToRegister.toEntity(user);
		userRepository.save(userInfo);
		return true;
	}
	
	@Override
	public void checkIdDuplicate(String id) {
		if(userRepository.existsByStudentNumber(id))
			throw new IllegalArgumentException("ID 중복");
	}
	
	@Override
	public void checkNicknameDuplicate(String nickname) {
		if(userRepository.existsByNickname(nickname))
			throw new IllegalArgumentException("닉네임 중복");
	}
}
