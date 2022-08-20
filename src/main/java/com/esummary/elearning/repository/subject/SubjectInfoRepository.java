package com.esummary.elearning.repository.subject;

import java.util.Optional;

import javax.persistence.TypedQuery;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.SubjectInfo;


@Repository
public interface SubjectInfoRepository extends CrudRepository<SubjectInfo, String>{
	@Query(value="Select si From SubjectInfo si Where si.subjectId = :subjectId")
	Optional<SubjectInfo> findSingleSubject(@Param("subjectId")String subjectId);
	
//	@EntityGraph(value = "all-detail-data") 
//	@Query(value = "Select si "
//			+ "From SubjectInfo si "
//			+ "join TaskInfo ti on si.subjectId = ti.subjectId "
//			+ "join NoticeInfo ni on si.subjectId = ni.subjectId "
//			+ "join WeekInfo wi on si.subjectId = wi.subjectId "
//			+ "join LectureInfo li on wi.weekId = li.lecture_weekId "
//		+ "Where si.subjectId = :subjectId")
	@Query(value="Select * "
			+ "From subject_info si join task_info ti on si.subject_id = ti.subject_id "
			+ "					Join notice_info ni on si.subject_id = ni.subject_id "
			+ "					join week_info wi on si.subject_id = wi.subject_id "
			+ "					join lecture_info li on wi.week_id = li.lecture_week_id "
			+ "where si.subject_id = '202214043CMP743';", 
			nativeQuery = true)
	SubjectInfo findBySubjectId(@Param("subjectId")String subjectId);
}
