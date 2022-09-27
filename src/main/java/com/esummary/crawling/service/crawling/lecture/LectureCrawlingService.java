package com.esummary.crawling.service.crawling.lecture;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserSubject;

public interface LectureCrawlingService {
	List<LectureInfo> getLectureList(Elements lectureElements, String lectureWeekId);
}
