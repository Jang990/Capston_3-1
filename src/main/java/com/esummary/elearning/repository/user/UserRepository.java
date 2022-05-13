package com.esummary.elearning.repository.user;

import java.util.List;

import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

import net.bytebuddy.asm.Advice.This;


@Repository
public interface UserRepository extends CrudRepository<UserInfo, String>{
	UserInfo findByStudentNumber(String studentNumber);
	@EntityGraph(
			attributePaths = {
					"userSubjects"
			}
	)
	List<UserInfo> findWithUserSubjectsByStudentNumber(String studentNumber);
}
