package com.esummary.elearning.repository.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserTask;


@Repository
public interface UserTaskRepository extends CrudRepository<UserTask, String>{

	Optional<UserTask> findBySubjectTaskInfo(SubjectTaskInfo subjectTaskInfo);
	Optional<UserTask> findByUserSubject_usIdAndSubjectTaskInfo_TaskId(long usId, String taskId);
	
}
