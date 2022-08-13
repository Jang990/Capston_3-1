package com.esummary.elearning.service.dao.lectures;

import java.util.List; 
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;

public interface DBLectureWeekUtil {
	//리팩토링하면서 추가되는 것
	boolean saveService(WeekInfo  lectureWeek);
	boolean saveService(List<WeekInfo> lectureWeeks);
	boolean validateDuplicate(WeekInfo lectureWeek);
}
