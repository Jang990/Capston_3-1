package com.esummary.elearning.dao.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;

@Component
public class UserInfoUtil_DB implements DBUserInfoUtil {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public boolean saveUserService(UserInfo user) {
		if(validateDuplicate(user))
			return false;
		
		userRepository.save(user);
		return true;
	}
	
	@Override
	public boolean validateDuplicate(UserInfo user) {
		//중복 확인
		Optional<UserInfo> check =  getUser(user.getStudentNumber(), user.getUserName());
		if(check.isPresent()) return true; //중복 맞음  
		else return false;
	}
	
	public Optional<UserInfo> getUser(String studentNumber, String userName) {
		return userRepository.findByStudentNumberAndUserName(studentNumber, userName);
	}

}
