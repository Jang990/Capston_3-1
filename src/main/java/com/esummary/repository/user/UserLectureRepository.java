package com.esummary.repository.user;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.entity.user.UserTask;


@Repository
public interface UserLectureRepository extends CrudRepository<UserLecture, String>{
	UserLecture findBySubjectLecture_LectureId(long lectureId);
	UserLecture findBySubjectLecture(LectureInfo lecture);
	
	Optional<UserLecture> findByUserSubject_usIdAndSubjectLecture_lectureId(long usId, long lectureId);
}
