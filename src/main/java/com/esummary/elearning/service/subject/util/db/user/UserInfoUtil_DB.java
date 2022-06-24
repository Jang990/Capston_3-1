package com.esummary.elearning.service.subject.util.db.user;

import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;

@Component
public class UserInfoUtil_DB implements DBUserInfoUtil {

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
		UserInfo check = userRepository.
				findByStudentNumberAndUserName(user.getStudentNumber(), user.getUserName());
		
		if(check == null) return false; 
		else return true; //중복 맞음
	}

}
