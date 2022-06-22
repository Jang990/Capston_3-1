package com.esummary.elearning.repository.subject;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;


@Repository
public interface SubjectLectureWeekRepository extends CrudRepository<SubjectLectureWeekInfo, String>{

	List<SubjectLectureWeekInfo> findBySubjectInfo(SubjectInfo subjectInfo);
	List<SubjectLectureWeekInfo> findBySubjectInfo_subjectId(String subjectId);

}
