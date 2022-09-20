package com.esummary.elearning.exservice.crawling.week;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface WeekCrawlingService {
	List<WeekInfo> getSubjectLectureWeekInfo(String subjectId, Map<String, String> initialCookies);
}
