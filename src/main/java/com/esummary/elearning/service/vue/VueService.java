package com.esummary.elearning.service.vue;

import java.util.List;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.LectureData;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface VueService {
//	List<SubjectCardData> getInitCardData(String studentNumber);getInitCardDatagetInitCardData
	
	//데이터베이스 조회
	List<LectureWeekData> getLectureData(String subjectId, String studentNumber);
	List<NoticeData> getNoticeData(String subjectId);
	List<TaskData> getTaskData(String subjectId, String studentNumber);
	
	//크롤링
	InitalPageData crawlInitData(UserData user);
	List<NoticeData> crawlNotice(UserData user, String subjectId);
	List<TaskData> crawlTask(UserData user, String subjectId);
	List<LectureWeekData> crawlLecture(UserData user, String subjectId);
	List<SubjectCardData> getSubjectDTO(List<SubjectInfo> subjects);
	List<UserSubject> searchUserSubject(String studentNumber);
	
	boolean saveUser(UserData user);
	
}
