package com.esummary.crawling.service;

import java.util.List;

import com.esummary.crawling.dto.LectureData;
import com.esummary.crawling.dto.LectureWeekData;
import com.esummary.crawling.dto.NoticeData;
import com.esummary.crawling.dto.TaskData;
import com.esummary.crawling.dto.exInitalPageData;
import com.esummary.crawling.dto.exSubjectCardData;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserSubject;

public interface exVueService {
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
