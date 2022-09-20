package com.esummary.elearning.exservice.user;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.elearning.exdto.user.UserToRegister;

public interface UserService {
	boolean exRegisterUser(UserData user);
	boolean registerUser(UserToRegister user);
	void checkIdDuplicate(String id);
	void checkNicknameDuplicate(String nickname);
}
