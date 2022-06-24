package com.esummary.elearning.service.subject.util.db;

import java.util.List;

import com.esummary.elearning.entity.subject.SubjectInfo;

public interface DBSubjectUtil {
	//저장 서비스 (중복 체크 + 저장)
	boolean saveService(SubjectInfo subject);
	boolean saveService(List<SubjectInfo> subjects);
	boolean validateDuplicate(SubjectInfo subject);
}
