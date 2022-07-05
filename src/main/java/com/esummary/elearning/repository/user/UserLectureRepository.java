package com.esummary.elearning.repository.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserTask;


@Repository
public interface UserLectureRepository extends CrudRepository<UserLecture, String>{
	UserLecture findBySubjectLecture_LectureId(long lectureId);
	UserLecture findBySubjectLecture(SubjectLecture lecture);
	
	Optional<UserLecture> findByUserSubject_usIdAndSubjectLecture_lectureId(long usId, long lectureId);
}
