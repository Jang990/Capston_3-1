package com.esummary.elearning.service.test;

import java.util.List;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

public interface TestService {
	// 그냥 단위테스트를 위한 서비스 인터페이스
	
	List<SubjectInfo> test(UserInfo user);
	UserInfo getUserInfo(String studentNumber);
}
