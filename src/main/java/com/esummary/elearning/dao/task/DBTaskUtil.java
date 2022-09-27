package com.esummary.elearning.dao.task;

import java.util.List; 

import org.springframework.stereotype.Component;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.TaskInfo;
import com.esummary.entity.user.UserTask;

@Component
public interface DBTaskUtil {
//	List<SubjectTaskInfo> getSubjectTaskInfo(UserSubject userSubject, Document docStudyHome, SubjectInfo subjectInfo);
	List<TaskInfo> getSubjectTaskInfo(SubjectInfo subjectInfo);

	List<UserTask> getUserTask(List<TaskInfo> taskList);
	
	boolean saveService(TaskInfo  task);
	boolean saveService(List<TaskInfo> tasks);
	boolean validateDuplicate(TaskInfo tasks);
}
