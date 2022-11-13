package com.esummary.repository;

import java.util.List;  
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.esummary.entity.user.UserInfo;
import com.esummary.entity.user.UserSubject;
import com.esummary.repository.querydsl.UserSubjectRepositoryCustom;


@Repository
public interface UserSubjectRepository extends JpaRepository<UserSubject, String>, UserSubjectRepositoryCustom{
	List<UserSubject> findByUserInfo(UserInfo user);
	
	Optional<UserSubject> findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(String subjectId, String studentNumber);
	
	/**
	 * UserSubject, UserTask, TaskInfo 테이블을 조인해서 가져옴 <br>
	 * |us_id |ut_id |task_id |subject_id|student_number |submityn |task_id |us_id |us_id |ut_id |description |end_date |not_submitted_num |start_date |subject_id |submission_num |title |total_num
	 */
	@EntityGraph(value = "user-own-task") 
	Optional<UserSubject> findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(String subjectId, String studentNumber);
	
//	@EntityGraph(value = "user-lecture-week") 
//	@EntityGraph(value = "test") 
	Optional<UserSubject> findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(String subjectId, String studentNumber);
	List<UserSubject> findByUserInfo_StudentNumber(String studentNumber);
	
	boolean existsByUserInfo_StudentNumberAndSubjectInfo_SubjectId(String studentNumber, String subjectId);
	
	boolean deleteByUserInfo_StudentNumberAndSubjectInfo_SubjectId(String studentNumber, String subjectId);
}