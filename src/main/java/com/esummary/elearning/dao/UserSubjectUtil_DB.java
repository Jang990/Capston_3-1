package com.esummary.elearning.dao;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.dao.lectures.DBLectureWeekUtil;
import com.esummary.elearning.dao.notice.DBNoticeUtil;
import com.esummary.elearning.dao.task.DBTaskUtil;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;

@Component("DB")
public class UserSubjectUtil_DB implements DBUserSubjectUtil{
	
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	
	@Override
	public boolean saveService(UserSubject userSubject) {
		if(validateDuplicate(userSubject)) // 중복 확인, 중복일시 예외발생
			return false;
		
		userSubjectRepository.save(userSubject); // DB 저장
		return true;
	}
	
	@Override
	public boolean saveService(List<UserSubject> userSubjects) {
		List<UserSubject> savedUserSubject = new ArrayList<UserSubject>();
		
		for (UserSubject userSubject : userSubjects) {
			if(validateDuplicate(userSubject)) // 중복 확인, 중복일시 예외발생
				continue;
			else {
				savedUserSubject.add(userSubject);
			}
		}
		
		if(savedUserSubject.size() == 0) return false;
		
		userSubjectRepository.saveAll(savedUserSubject); // DB 저장
		return true;
	}

	@Override
	public boolean validateDuplicate(UserSubject userSubject) {
		//auto Increment가 기본키이기 때문에 과목번호랑 학번이 중복되는건 저장하면 안된다. 
		Optional<UserSubject> us = userSubjectRepository.findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(
				userSubject.getSubjectId(), userSubject.getStudentNumber());
		
		if(us.isEmpty()) return false;
		else return true; //중복 맞음
	}

	@Override
	public Optional<UserSubject> getStudentSubject(String subjectId, String studentNumber) {
		return userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
	}
	
	@Override
	public List<UserSubject> getStudentsSubject(String studentNumber) {
		return userSubjectRepository.findByUserInfo_StudentNumber(studentNumber);
	}
	
}
