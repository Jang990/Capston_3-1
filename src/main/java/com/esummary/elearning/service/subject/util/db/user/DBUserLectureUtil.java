package com.esummary.elearning.service.subject.util.db.user;

import java.util.List;

import com.esummary.elearning.entity.user.UserLecture; 


public interface DBUserLectureUtil {
	//저장 서비스 (중복 체크 + 저장)
	boolean saveService(UserLecture subject);
	boolean saveService(List<UserLecture> subjects);
	boolean validateDuplicate(UserLecture subject);
	
	UserLecture getUserLecture(long usId, long lectureId);
}
