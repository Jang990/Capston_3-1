package com.esummary.user.service;

import com.esummary.user.dto.UserDTO;

public interface UserService {
	/**
	 * 기본적인 학번, 닉네임, 가입일을 반환해주는 메서드
	 * @param studentId
	 * @return
	 */
	UserDTO getUserData(String studentId);
}
