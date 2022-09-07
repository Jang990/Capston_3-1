package com.esummary.elearning.service.user;

import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.user.UserInfo;

public interface UserService {
	boolean registerUser(UserData user);
	boolean checkIdDuplicate(String id);
	boolean checkNicknameDuplicate(String nickname);
}
