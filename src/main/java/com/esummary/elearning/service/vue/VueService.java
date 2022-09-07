package com.esummary.elearning.service.vue;

import java.util.List;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.LectureData;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.user.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface VueService {
	/*
	 * 프론트엔드(Vue)에서 Axios로 요청하는 것을 처리하는 서비스
	 * 이건 나누는게 필요할 듯. 겹친다.
	 */
//	List<SubjectCardData> getInitCardData(String studentNumber);getInitCardDatagetInitCardData
	
	//크롤링
	InitalPageData crawlInitDataService(UserData user);
	List<NoticeData> crawlNotice(UserData user, String subjectId);
	List<TaskData> crawlTask(UserData user, String subjectId);
	List<LectureWeekData> crawlLecture(UserData user, String subjectId);
	
	//리팩토링 중 만들어지는 것
//	boolean saveInitData(UserData userDTO, InitalPageData initPageData);
	
}
