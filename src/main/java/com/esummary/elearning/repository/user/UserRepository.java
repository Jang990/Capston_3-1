package com.esummary.elearning.repository.user;

import java.util.List;
import java.util.Optional;

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
	Optional<UserInfo> findByStudentNumber(String studentNumber);
	Optional<UserInfo> findByStudentNumberAndNickname(String studentNumber, String nickname);
	boolean existsByStudentNumber(String studentNumber);
	boolean existsByNickname(String nickname);

//	@EntityGraph(value = "user-own-subject") 
	@EntityGraph(value = "user-own-subject-Detail") 
	Optional<UserInfo> findWithUserSubjectsByStudentNumber(String studentNumber);
}
