package com.esummary.elearning.service.crawling.week;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface WeekCrawlingService {
	List<SubjectLectureWeekInfo> getSubjectLectureWeekInfo(String subjectId, Map<String, String> initialCookies);
}
