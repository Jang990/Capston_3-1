package com.esummary.elearning.service.login;

import java.util.List;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

public interface LoginService {
	UserInfo login(String id, String password);
}
