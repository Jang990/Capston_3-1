package com.esummary.elearning.exservice.vue;

import java.util.List;

import com.esummary.crawling.dto.NoticeData;
import com.esummary.crawling.dto.TaskData;
import com.esummary.crawling.dto.exInitalPageData;
import com.esummary.crawling.dto.exSubjectCardData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.exdto.subject.LectureData;
import com.esummary.elearning.exdto.subject.LectureWeekData;
import com.esummary.elearning.exdto.user.UserData;

public interface VueService {
	/*
	 * 프론트엔드(Vue)에서 Axios로 요청하는 것을 처리하는 서비스
	 * 이건 나누는게 필요할 듯. 겹친다.
	 */
//	List<SubjectCardData> getInitCardData(String studentNumber);getInitCardDatagetInitCardData
	
	//크롤링
	exInitalPageData crawlInitDataService(UserData user);
	List<NoticeData> crawlNotice(UserData user, String subjectId);
	List<TaskData> crawlTask(UserData user, String subjectId);
	List<LectureWeekData> crawlLecture(UserData user, String subjectId);
	
	//리팩토링 중 만들어지는 것
//	boolean saveInitData(UserData userDTO, InitalPageData initPageData);
	
}
