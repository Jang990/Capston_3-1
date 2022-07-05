package com.esummary.elearning.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository; 
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;


@Repository
public interface UserSubjectRepository extends CrudRepository<UserSubject, String>{
	List<UserSubject> findByUserInfo(UserInfo user);
	
	Optional<UserSubject> findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(String subjectId, String studentNumber);
	
	@EntityGraph(value = "user-own-task") 
	Optional<UserSubject> findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(String subjectId, String studentNumber);
	
	@EntityGraph(value = "user-lecture-week") 
	Optional<UserSubject> findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(String subjectId, String studentNumber);
	List<UserSubject> findByUserInfo_StudentNumber(String studentNumber);
	
}