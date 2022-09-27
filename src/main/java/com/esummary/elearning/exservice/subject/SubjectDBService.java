package com.esummary.elearning.exservice.subject;

import java.util.List;
import java.util.Map;

import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.crawling.dto.tofront.SubjectDetailDataWithCnt_DTO;
import com.esummary.crawling.dto.tofront.TaskData;
import com.esummary.elearning.exdto.user.UserData;


public interface SubjectDBService {
	//데이터베이스 조회
	List<SubjectDetailDataWithCnt_DTO> getSubjectDataWithAllDetail(UserData user, String studentNumber);
	List<LectureWeekData> getLectureData(UserData user, String studentNumber);
	List<NoticeData> getNoticeData(String subjectId);
	List<TaskData> getTaskData(UserData user, String studentNumber);
}
