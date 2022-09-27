package com.esummary.subject.service;

import java.util.List;

import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.elearning.dto.subject.SubjectDTO;

/**
 * 사용자 정보를 제외한 순수 과목의 정보를 제공하는 서비스
 * @author User
 *
 */
public interface SubjectService {
	SubjectDTO getSubject(String subjectId);
	
	/**
	 * 공지사항 가져오기
	 */
	List<NoticeData> getNoticeData(String subjectId);
}
