package com.esummary.subject.service;

import java.util.List;

import com.esummary.crawling.dto.tofront.NoticeData;

/**
 * 사용자 정보를 포함한 과목의 정보와 사용자 정보(강의 시청시간, 과제 제출 여부 등)를 제공하는 서비스 
 * <br>
 * 그러므로 순수한 Subject 값을 전해주는 SubjectService에 기능을 사용하면서 거기에 사용자 데이터를 추가
 * 
 * @author User
 *
 */
public interface UserSubjectService {

	/**
	 * 사용자가 과목을 수강하고 있는지 체크
	 * <br>
	 * 만약 DB에 일치하는 것이 없다면 IllegalArgumentException을 던짐
	 * 
	 */
	void checkUserOwnSubject(String studentId, String subjectId);
	
	/**
	 * 기본적으로는 Notice는 사용자 정보가 필요없음 <br>
	 * 공지는 제출여부 같은게 없이 정보문이기 때문
	 */
	List<NoticeData> getNoticeData(String subjectId);
	
}
