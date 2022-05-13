package com.esummary.elearning.service.test;

import java.util.List;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

public interface TestService {
	List<SubjectInfo> test(UserInfo user);
	UserInfo getUserInfo(String studentNumber);
}
