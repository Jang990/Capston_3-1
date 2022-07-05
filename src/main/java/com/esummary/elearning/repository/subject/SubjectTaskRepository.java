package com.esummary.elearning.repository.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
 

@Repository
public interface SubjectTaskRepository extends CrudRepository<SubjectTaskInfo, String>{
	List<SubjectTaskInfo> findBySubjectInfo(SubjectInfo subjectInfo);
	List<SubjectTaskInfo> findBySubjectInfo_SubjectId(String subjectId);
	
	Optional<SubjectTaskInfo> findByTaskId(String taskId);
}
