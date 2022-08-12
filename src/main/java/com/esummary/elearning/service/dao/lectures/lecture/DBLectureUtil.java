package com.esummary.elearning.service.dao.lectures.lecture;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;

public interface DBLectureUtil {
	List<SubjectLecture> getLectureList(SubjectLectureWeekInfo subjectLectureWeekInfo); //이것도 없애야할듯

	Optional<UserLecture> getUserLecture(int usId, SubjectLecture lecture);
	
	//리팩토링하면서 추가되는 것
	boolean saveService(SubjectLecture  lecture);
	boolean saveService(List<SubjectLecture> lectures);
	boolean validateDuplicate(SubjectLecture lecture);
	
	Optional<SubjectLecture> getLecture(String lectureWeekId, String idx);
}
