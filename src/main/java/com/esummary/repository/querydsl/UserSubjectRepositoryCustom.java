package com.esummary.repository.querydsl;

import java.util.List;

import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserSubject;

public interface UserSubjectRepositoryCustom {
	List<UserSubject> findLectureInfo(String studentId,String subjectId);
}
