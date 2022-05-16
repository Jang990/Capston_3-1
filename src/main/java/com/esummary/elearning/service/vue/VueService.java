package com.esummary.elearning.service.vue;

import java.util.List; 

import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;

public interface VueService {
	List<SubjectCardData> getInitCardData(String studentNumber);
	List<NoticeData> getNoticeData(String subjectId);
	List<TaskData> getTaskData(String subjectId, String studentNumber);
	List<LectureWeekData> getLectureeData(String subjectId, String studentNumber);
}
