package com.esummary.elearning.repository.subject;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;


@Repository
public interface SubjectRepository extends CrudRepository<SubjectInfo, String>{
	SubjectInfo findBySubjectId(String subjectId);
}
