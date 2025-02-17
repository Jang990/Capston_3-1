package com.esummary.repository.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;


@Repository
public interface WeekInfoRepository extends CrudRepository<WeekInfo, String>{

	List<WeekInfo> findBySubjectInfo(SubjectInfo subjectInfo);
	List<WeekInfo> findBySubjectInfo_subjectId(String subjectId);
	
	Optional<WeekInfo> findByWeekIdAndSubjectInfo_subjectId(String weekId, String subjectId);

}
