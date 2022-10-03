package com.esummary.auth.service.User;

import com.esummary.auth.dto.SignUpUserDTO;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.user.UserInfo;

public interface SignUpService {
	/**
	 * 이전 회원가입 로직. - 삭제될 예정
	 * @param user
	 * @return
	 */
	boolean exRegisterUser(UserData user);
	
	/**
	 * 회원가입
	 * @param signupUserDto
	 * @return
	 */
	SignUpUserDTO signup(SignUpUserDTO signupUserDto);
	
	/**
	 * 회원 ID 중복 체크
	 * @param id
	 */
	void checkIdDuplicate(String id);
	
	/**
	 * 회원 닉네임 중복 체크
	 * @param nickname
	 */
	void checkNicknameDuplicate(String nickname);
	/**
	 * 이러닝 로그인이 가능한지 체크
	 * @param studentId
	 * @param password
	 */
	void checkElearningLoginCookie(String studentId, String password);
}
