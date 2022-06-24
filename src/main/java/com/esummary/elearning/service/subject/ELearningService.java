package com.esummary.elearning.service.subject;

import java.util.List;

import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

public interface ELearningService {
	/*
	 *  이러닝에서 일어나는 일을 총괄하는 서비스
	 *  예시) 크롤링, 페이지 이동 등등
	 */
	
	List<SubjectInfo> summary(UserInfo user);

	//이건 너무 많은 역할을 하나의 메서드에서 하고 있다. crawl과 save로 나누고 삭제할 것이다.
//	List<SubjectInfo> crawlAndSaveBasicSubjectData(UserData user);
	
//	List<SubjectInfo> crawlBasicSubjectData(UserData user);
//	boolean saveBasicSubjectData(UserData user, List<SubjectInfo> subjects);



}
