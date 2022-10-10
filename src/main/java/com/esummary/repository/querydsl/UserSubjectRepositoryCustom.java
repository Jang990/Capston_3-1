package com.esummary.repository.querydsl;

import java.util.List;

import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.TaskData;
import com.esummary.entity.user.UserSubject;

public interface UserSubjectRepositoryCustom {
	List<LectureWeekData> findUserLectureList(String studentId, String subjectId);
	List<TaskData> findUserTaskList(String studentId, String subjectId);
}
