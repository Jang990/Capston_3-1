package com.esummary.repository.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.TaskInfo;
 

@Repository
public interface TaskInfoRepository extends CrudRepository<TaskInfo, String>{
	List<TaskInfo> findBySubjectInfo(SubjectInfo subjectInfo);
	List<TaskInfo> findBySubjectInfo_SubjectId(String subjectId);
	
	Optional<TaskInfo> findByTaskId(String taskId);
}
