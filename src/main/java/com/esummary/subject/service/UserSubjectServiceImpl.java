package com.esummary.subject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.crawling.dto.tofront.TaskData;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.entity.user.UserSubject;
import com.esummary.entity.user.UserTask;
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
		if(!userSubjectRepository.existsByUserInfo_StudentNumberAndSubjectInfo_SubjectId(studentId, subjectId))
			throw new IllegalArgumentException("사용자가 해당 과목을 가지고 있지 않습니다. 학번 ="+ studentId +", 과목ID ="+subjectId);
	}

	@Override
	public List<NoticeData> getNoticeData(String subjectId) {
		// notice는 사용자 개인정보가 필요없다.
		return subjectService.getNoticeData(subjectId);
	}
	
	@Override
	public List<TaskData> getTaskData(String studentId, String subjectId) {
		checkUserOwnSubject(studentId, subjectId);
		return userSubjectRepository.findUserTaskList(studentId, subjectId);
	}
	
	@Override
	public List<LectureWeekData> getLectureData(String studentId, String subjectId) {
		UserSubject us = userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 과목이 존재하지 않음. 학번: "+studentId+", 과목ID: "+subjectId));		
		
		List<LectureWeekData> weekDTO = userSubjectRepository.findUserLectureList(studentId, subjectId);
		for (LectureWeekData lectureWeekData : weekDTO) {
			lectureWeekData.calcCnt();
		}
		
		return weekDTO;
	}
}
