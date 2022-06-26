package com.esummary.elearning.repository.subject;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;


@Repository
public interface SubjectLectureRepository extends CrudRepository<SubjectLecture, String>{

	List<SubjectLecture> findBySubjectLectureWeekInfo(SubjectLectureWeekInfo subjectLectureWeekInfo);

	SubjectLecture findBySubjectLectureWeekInfo_LectureWeekIdAndIdx(String lectureWeekId, String idx);

}
