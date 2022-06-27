package com.esummary.elearning.service.subject.util.db.user;

import java.util.ArrayList; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.repository.user.UserTaskRepository;

@Component
public class UserTaskUtil_DB implements DBUserTaskUtil {
	
	@Autowired
	private UserTaskRepository userTaskRepository;
	
	@Override
	public boolean saveService(UserTask userLecture) {
		if(validateDuplicate(userLecture))
			return false;
		
		userTaskRepository.save(userLecture);
		return true;
	}
	
	@Override
	public boolean saveService(List<UserTask> userLectures) {
		List<UserTask> savedSubjects = new ArrayList<UserTask>();
		
		for (UserTask userLecture : userLectures) {
			if(validateDuplicate(userLecture)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedSubjects.add(userLecture);
		}
		
		if(savedSubjects.size() == 0) return false;
		
		userTaskRepository.saveAll(savedSubjects);
		return true;
	}

	@Override
	public boolean validateDuplicate(UserTask userLecture) {
		//UserLecture와 UserTask 의 경우 중복 체크가 다름
		UserTask userLectureCheck = userTaskRepository.
				findByUserSubject_usIdAndSubjectTaskInfo_TaskId(userLecture.getUserSubjectId(), userLecture.getSubjectTaskId());
		
		if(userLectureCheck == null || checkEntityValue(userLecture, userLectureCheck)) return false;
		return true; //중복 맞음
	}
	
	//두 UserLecture Entity의 실제 값을 비교.
	private boolean checkEntityValue(UserTask userTask1, UserTask userTask2) {
		if(userTask1.getSubmitYN().equals(userTask2.getSubmitYN()))
			return true; // 실제 값이 같음.
		else
			return false;
	}

//	@Override
//	public UserTask getUserLecture(long usId, long lectureId) {
//		UserLecture ul = userTaskRepository.findByUserSubject_usIdAndSubjectLecture_lectureId(usId, lectureId);
//		return ul;
//	}
	
	
}
