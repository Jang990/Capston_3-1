package com.esummary.elearning.dao.user;

import java.util.List;
import java.util.Optional;

import com.esummary.elearning.entity.user.UserTask; 


public interface DBUserTaskUtil {
	//저장 서비스 (중복 체크 + 저장)
	boolean saveService(UserTask subject);
	boolean saveService(List<UserTask> subjects);
	boolean validateDuplicate(UserTask subject);
	Optional<UserTask> getUserTask(long usId, String taskId);
}
