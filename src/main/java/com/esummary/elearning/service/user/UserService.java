package com.esummary.elearning.service.user;

import com.esummary.elearning.dto.user.UserData;
import com.esummary.elearning.dto.user.UserToRegister;
import com.esummary.elearning.entity.user.UserInfo;

public interface UserService {
	boolean exRegisterUser(UserData user);
	boolean registerUser(UserToRegister user);
	void checkIdDuplicate(String id);
	void checkNicknameDuplicate(String nickname);
}
