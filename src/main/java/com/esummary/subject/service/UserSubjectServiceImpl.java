package com.esummary.subject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.entity.subject.NoticeInfo;
import com.esummary.repository.UserSubjectRepository;
import com.esummary.repository.subject.NoticeInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSubjectServiceImpl implements UserSubjectService {
	
	private final UserSubjectRepository userSubjectRepository;
	private final SubjectService subjectService;
	
	@Override
	public void checkUserOwnSubject(String studentId, String subjectId) {
		if(!userSubjectRepository.existsByUserInfo_StudentNumberAndSubjectInfo_SubjectId(subjectId, studentId))
			throw new IllegalArgumentException("사용자가 해당 과목을 가지고 있지 않습니다. 학번 ="+ studentId +", 과목ID ="+subjectId);
	}

	@Override
	public List<NoticeData> getNoticeData(String subjectId) {
		return subjectService.getNoticeData(subjectId);
	}
}
