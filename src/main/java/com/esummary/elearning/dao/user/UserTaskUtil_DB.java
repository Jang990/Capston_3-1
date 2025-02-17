package com.esummary.elearning.dao.user;

import java.util.ArrayList;  
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.entity.user.UserTask;
import com.esummary.repository.user.UserTaskRepository;

@Component
public class UserTaskUtil_DB implements DBUserTaskUtil {

	
	@Autowired
	private UserTaskRepository userTaskRepository;
	
	@Override
	public boolean saveService(UserTask userTask) {
		if(validateDuplicate(userTask))
			return false;
		
		userTaskRepository.save(userTask);
		return true;
	}
	
	@Override
	public boolean saveService(List<UserTask> userTasks) {
		List<UserTask> savedTasks = new ArrayList<UserTask>();
		
		for (UserTask userTask : userTasks) {
			if(validateDuplicate(userTask)) // 중복 확인, 중복일시 예외발생
				continue;
			else {
				savedTasks.add(userTask);
			}
		}
		
		if(savedTasks.size() == 0) return false;
		
		userTaskRepository.saveAll(savedTasks);
		return true;
	}

	@Override
	public boolean validateDuplicate(UserTask userTask) {
		//UserLecture와 UserTask 의 경우 중복 체크가 다름
		Optional<UserTask> userLectureCheck = getUserTask(userTask.getUserSubjectId(), userTask.getSubjectTaskId());
		
		if(userLectureCheck.isEmpty()) return false;
		if(!equalEntityValue(userTask, userLectureCheck.get())) return false;
		return true; //중복 맞음
	}
	
	//두 UserLecture Entity의 실제 값을 비교.
	private boolean equalEntityValue(UserTask userTask1, UserTask userTask2) {
		if(userTask1.getSubmitYN().equals(userTask2.getSubmitYN()))
			return true; // 실제 값이 같음.
		else
			return false;
	}
	
	public Optional<UserTask> getUserTask(long usId, String taskId) {
		return userTaskRepository.
				findByUserSubject_usIdAndTaskInfo_TaskId(usId, taskId);
	}

//	@Override
//	public UserTask getUserLecture(long usId, long lectureId) {
//		UserLecture ul = userTaskRepository.findByUserSubject_usIdAndSubjectLecture_lectureId(usId, lectureId);
//		return ul;
//	}
	
	
}
