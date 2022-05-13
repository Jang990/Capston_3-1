package com.esummary.elearning.repository.subject;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
 

@Repository
public interface SubjectTaskRepository extends CrudRepository<SubjectTaskInfo, String>{
	List<SubjectTaskInfo> findBySubjectInfo(SubjectInfo subjectInfo);
}
