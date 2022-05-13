package com.esummary.elearning.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository; 
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;

/*
@Repository
public interface UserSubjectRepository extends CrudRepository<UserSubject, UserSubject_Id>{

}
*/

@Repository
public interface UserSubjectRepository extends CrudRepository<UserSubject, String>{
	List<UserSubject> findByUserInfo(UserInfo user);
	
//	List<UserSubject> findByStudentNumber(String studentNumber);
}