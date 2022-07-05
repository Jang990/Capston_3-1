package com.esummary.elearning.service.subject.util.db.user;

import java.util.ArrayList; 
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.repository.user.UserLectureRepository;

@Component
public class UserLectureUtil_DB implements DBUserLectureUtil {
	
	private static long seq_UserLec = 1L; //임시로 UserLecture를 DB에 등록하기위해 만들어놓음 시퀀스사용할 것
	
	@Autowired
	private UserLectureRepository userLectureRepository;
	
	@Override
	public boolean saveService(UserLecture userLecture) {
		if(validateDuplicate(userLecture))
			return false;
		
		userLecture.setUlId(seq_UserLec++); // MySQL로 바꾸고 삭제
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
				userLecture.setUlId(seq_UserLec++); // MySQL로 바꾸고 삭제
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
