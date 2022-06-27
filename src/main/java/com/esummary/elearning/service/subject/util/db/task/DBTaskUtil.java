package com.esummary.elearning.service.subject.util.db.task;

import java.util.List; 

import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserTask;

@Component
public interface DBTaskUtil {
//	List<SubjectTaskInfo> getSubjectTaskInfo(UserSubject userSubject, Document docStudyHome, SubjectInfo subjectInfo);
	List<SubjectTaskInfo> getSubjectTaskInfo(SubjectInfo subjectInfo);

	List<UserTask> getUserTask(List<SubjectTaskInfo> taskList);
	
	boolean saveService(SubjectTaskInfo  task);
	boolean saveService(List<SubjectTaskInfo> tasks);
	boolean validateDuplicate(SubjectTaskInfo tasks);
}
