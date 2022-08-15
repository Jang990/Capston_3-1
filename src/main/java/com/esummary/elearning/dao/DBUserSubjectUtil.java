package com.esummary.elearning.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.nodes.Document;

import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface DBUserSubjectUtil {
	//저장 서비스 (중복 체크 + 저장)
	boolean saveService(UserSubject us);
	boolean saveService(List<UserSubject> us);
	
	boolean validateDuplicate(UserSubject userSubject);
	
	List<UserSubject> getStudentsSubject(String studentNumber);
	Optional<UserSubject> getStudentSubject(String subjectId, String studentNumber);
	
//	List<SubjectInfo> InitSubjectData(UserInfo user);
//	void getSubjectDetail(UserInfo user);
//	void settingUserSubject(UserInfo user, List<SubjectInfo> subjectList);

//	List<SubjectInfo> crawlAndSaveBasicSubjectData(UserInfo user);

//	boolean saveBasicSubject(UserInfo user, List<SubjectInfo> subjectList);

//	List<SubjectInfo> crawlSubjectInfo(UserInfo user);
}
