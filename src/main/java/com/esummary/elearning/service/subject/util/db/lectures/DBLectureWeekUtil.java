package com.esummary.elearning.service.subject.util.db.lectures;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;

public interface DBLectureWeekUtil {
	List<SubjectLectureWeekInfo> getSubjectLectureInfo(SubjectInfo subjectInfo);
	List<SubjectLectureWeekInfo> getSubjectLectureInfo(String subjectId);

	List<UserLecture> getUserlecture(UserSubject us, List<SubjectLectureWeekInfo> lectureList);

}
