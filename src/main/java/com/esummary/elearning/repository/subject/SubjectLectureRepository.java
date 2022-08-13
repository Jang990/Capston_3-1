package com.esummary.elearning.repository.subject;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;


@Repository
public interface SubjectLectureRepository extends CrudRepository<LectureInfo, String>{

	List<LectureInfo> findByWeekInfo(WeekInfo subjectLectureWeekInfo);
	
	Optional<LectureInfo> findByWeekInfo_WeekIdAndIdx(String weekId, String idx);

}
