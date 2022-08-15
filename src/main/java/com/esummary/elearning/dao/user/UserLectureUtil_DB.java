package com.esummary.elearning.dao.user;

import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

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
			else {
				savedSubjects.add(userLecture);
			}
		}
		
		if(savedSubjects.size() == 0) return false;
		
		userLectureRepository.saveAll(savedSubjects);
		return true;
	}

	@Override
	public boolean validateDuplicate(UserLecture userLecture) {
		//UserLecture와 UserTask 의 경우 중복 체크가 다름
		Optional<UserLecture> userLectureCheck = getUserLecture(userLecture.getUserSubjectId(), userLecture.getSubjectLectureId());
		
		if(userLectureCheck.isEmpty() || checkEntityValue(userLecture, userLectureCheck.get())) return false;
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
	public Optional<UserLecture> getUserLecture(long usId, long lectureId) {
		return userLectureRepository.findByUserSubject_usIdAndSubjectLecture_lectureId(usId, lectureId);
	}
	
	
}
