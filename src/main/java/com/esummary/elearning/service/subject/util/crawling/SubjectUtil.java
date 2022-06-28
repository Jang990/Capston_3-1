package com.esummary.elearning.service.subject.util.crawling;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

public interface SubjectUtil {
	
//	List<SubjectInfo> getSubjectList(UserInfo user);
	
//	List<SubjectInfo> InitSubjectData(UserInfo user);
//	void getSubjectDetail(UserInfo user);
//	void settingUserSubject(UserInfo user, List<SubjectInfo> subjectList);

	List<SubjectInfo> crawlBasicSubjectInfo(Map<String, String> loginCookie);
}
