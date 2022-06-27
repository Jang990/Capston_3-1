package com.esummary.elearning.service.subject.util.db.user;

import java.util.ArrayList; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.repository.user.UserLectureRepository;

@Component
public class UserLectureUtil_DB implements DBUserLectureUtil {
	
	@Autowired
	private UserLectureRepository userLectureRepository;
	
	@Override
	public boolean saveService(UserLecture userLecture) {
		if(validateDuplicate(userLecture))
			return false;
		
		userLectureRepository.save(userLecture);
		return true;
	}
	
	@Override
	public boolean saveService(List<UserLecture> userLectures) {
		List<UserLecture> savedSubjects = new ArrayList<UserLecture>();
		
		for (UserLecture userLecture : userLectures) {
			if(validateDuplicate(userLecture)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedSubjects.add(userLecture);
		}
		
		if(savedSubjects.size() == 0) return false;
		
		userLectureRepository.saveAll(savedSubjects);
		return true;
	}

	@Override
	public boolean validateDuplicate(UserLecture userLecture) {
		//UserLecture와 UserTask 의 경우 중복 체크가 다름
		UserLecture userLectureCheck = userLectureRepository.
				findByUserSubject_usIdAndSubjectLecture_lectureId(userLecture.getUserSubjectId(), userLecture.getSubjectLectureId());
		
		if(userLectureCheck == null || checkEntityValue(userLecture, userLectureCheck)) return false;
		return true; //중복 맞음
	}
	
	//두 UserLecture Entity의 실제 값을 비교.
	private boolean checkEntityValue(UserLecture userLecture1, UserLecture userLecture2) {
		if(userLecture1.getLearningTime().equals(userLecture2.getLearningTime()) &&
				userLecture1.getStatus().equals(userLecture2.getStatus()) &&
				userLecture1.getUlId() == userLecture2.getUlId())
			return true; // 실제 값이 같음.
		else
			return false;
	}

	@Override
	public UserLecture getUserLecture(long usId, long lectureId) {
		UserLecture ul = userLectureRepository.findByUserSubject_usIdAndSubjectLecture_lectureId(usId, lectureId);
		return ul;
	}
	
	
}
