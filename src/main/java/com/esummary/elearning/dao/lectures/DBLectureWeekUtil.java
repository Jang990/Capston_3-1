package com.esummary.elearning.dao.lectures;

import java.util.List; 
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.entity.user.UserSubject;
import com.esummary.entity.user.UserTask;

public interface DBLectureWeekUtil {
	//리팩토링하면서 추가되는 것
	boolean saveService(WeekInfo  lectureWeek);
	boolean saveService(List<WeekInfo> lectureWeeks);
	boolean validateDuplicate(WeekInfo lectureWeek);
}
