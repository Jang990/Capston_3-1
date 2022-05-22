package com.esummary.elearning.service.subject;

import java.util.List;

import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

public interface ELearningService {

//	UserInfo login(String id, String password);

	List<SubjectInfo> summary(UserInfo user);

	List<SubjectCardData> crawlBasicSubjectData(UserData user);
}
