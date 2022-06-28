package com.esummary.elearning.service.subject.util.db;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.service.subject.util.db.lectures.DBLectureWeekUtil;
import com.esummary.elearning.service.subject.util.db.notice.DBNoticeUtil;
import com.esummary.elearning.service.subject.util.db.task.DBTaskUtil;

@Component("DB")
public class UserSubjectUtil_DB implements DBUserSubjectUtil{
	
	private static long seqUserSubjectNum = 0; // 시퀸스 넘버. MySQL로 바꾸고 auto Increment로 바꿀것
	
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	
	@Override
	public boolean saveService(UserSubject userSubject) {
		if(validateDuplicate(userSubject)) // 중복 확인, 중복일시 예외발생
			return false;
		
		userSubject.setUsId(seqUserSubjectNum++); //이거 삭제하고 MySQL 로 바꾸기 OK?
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
				userSubject.setUsId(seqUserSubjectNum++); //이거 삭제하고 MySQL 로 바꾸기 OK?
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
		UserSubject us = userSubjectRepository.findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(
				userSubject.getSubjectId(), userSubject.getStudentNumber());
		
		if(us == null) return false;
		else return true; //중복 맞음
	}

	@Override
	public UserSubject getStudentSubject(String subjectId, String studentNumber) {
		return userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
	}
	
	@Override
	public List<UserSubject> getStudentsSubject(String studentNumber) {
		return userSubjectRepository.findByUserInfo_StudentNumber(studentNumber);
	}
	
}
