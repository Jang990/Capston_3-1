package com.esummary.repository.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.TaskInfo;
import com.esummary.entity.user.UserInfo;
import com.esummary.entity.user.UserTask;


@Repository
public interface UserTaskRepository extends CrudRepository<UserTask, String>{

	Optional<UserTask> findByTaskInfo(TaskInfo subjectTaskInfo);
	Optional<UserTask> findByUserSubject_usIdAndTaskInfo_TaskId(long usId, String taskId);
	
}
