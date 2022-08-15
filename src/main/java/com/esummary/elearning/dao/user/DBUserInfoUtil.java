package com.esummary.elearning.dao.user;

import com.esummary.elearning.entity.user.UserInfo;

public interface DBUserInfoUtil {
	boolean saveUserService(UserInfo user);
	boolean validateDuplicate(UserInfo user);
}
