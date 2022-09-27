package com.esummary.crawling.service.crawling.week;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserSubject;

public interface WeekCrawlingService {
	List<WeekInfo> getSubjectLectureWeekInfo(String subjectId, Map<String, String> initialCookies);
}
