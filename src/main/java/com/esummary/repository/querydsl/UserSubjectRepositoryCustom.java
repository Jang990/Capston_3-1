package com.esummary.repository.querydsl;

import java.util.List;

import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.TaskData;

public interface UserSubjectRepositoryCustom {
	List<LectureWeekData> findUserLectureList(String studentId, String subjectId);
	List<TaskData> findUserTaskList(String studentId, String subjectId);
	
	/** 사용자가 가지고 있는 과목 정보를 subjectInfo에서 가져옴 */
	List<InhatcSubjectCardDTO> findUserOwnSubjectInfo(String studentId);
}
