package com.esummary.elearning.service.subject.util.db.user;

import java.util.List; 

import com.esummary.elearning.entity.user.UserTask; 


public interface DBUserTaskUtil {
	//저장 서비스 (중복 체크 + 저장)
	boolean saveService(UserTask subject);
	boolean saveService(List<UserTask> subjects);
	boolean validateDuplicate(UserTask subject);
}
