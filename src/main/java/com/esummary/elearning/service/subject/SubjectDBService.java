package com.esummary.elearning.service.subject;

import java.util.List;
import java.util.Map;

import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectDetailDataWithCnt_DTO;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.user.UserData;


public interface SubjectDBService {
	//데이터베이스 조회
	List<SubjectDetailDataWithCnt_DTO> getSubjectDataWithAllDetail(UserData user, String studentNumber);
	List<LectureWeekData> getLectureData(UserData user, String studentNumber);
	List<NoticeData> getNoticeData(String subjectId);
	List<TaskData> getTaskData(UserData user, String studentNumber);
}
