package com.esummary.elearning.repository.subject;

import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;


@Repository
public interface SubjectInfoRepository extends CrudRepository<SubjectInfo, String>{
	@Query(value="Select si From SubjectInfo si Where si.subjectId = :subjectId")
	Optional<SubjectInfo> findSingleSubject(@Param("subjectId")String subjectId);
	
//	@EntityGraph(value = "all-detail-data") 
	SubjectInfo findBySubjectId(String subjectId);
}
