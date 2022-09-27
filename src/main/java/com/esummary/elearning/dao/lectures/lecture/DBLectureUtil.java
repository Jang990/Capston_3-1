package com.esummary.elearning.dao.lectures.lecture;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.entity.user.UserSubject;

public interface DBLectureUtil {
	List<LectureInfo> getLectureList(WeekInfo subjectLectureWeekInfo); //이것도 없애야할듯

	Optional<UserLecture> getUserLecture(int usId, LectureInfo lecture);
	
	//리팩토링하면서 추가되는 것
	boolean saveService(LectureInfo  lecture);
	boolean saveService(List<LectureInfo> lectures);
	boolean validateDuplicate(LectureInfo lecture);
	
	Optional<LectureInfo> getLecture(String lectureWeekId, String idx);
}
