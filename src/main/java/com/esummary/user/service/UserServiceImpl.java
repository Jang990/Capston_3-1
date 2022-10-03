package com.esummary.user.service;

import org.springframework.stereotype.Service;

import com.esummary.entity.user.UserInfo;
import com.esummary.repository.user.UserRepository;
import com.esummary.user.dto.UserDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDTO getUserData(String studentId) {
		UserInfo user = userRepository.findByStudentNumber(studentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. -> 학번: " + studentId)
		);
		
		return UserDTO.builder()
				.studentId(user.getStudentNumber())
				.nickname(user.getNickname())
				.createdDate(user.getCreatedDate())
				.build();
	}

}
