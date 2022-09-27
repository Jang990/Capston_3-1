package com.esummary.subject.service;

import com.esummary.elearning.dto.subject.SubjectDTO;

/**
 * 사용자 정보를 제외한 순수 과목의 정보를 제공하는 서비스
 * @author User
 *
 */
public interface SubjectService {
	SubjectDTO getSubject(String subjectId);
}
