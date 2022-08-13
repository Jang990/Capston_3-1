package com.esummary.elearning.service.crawling.lecture;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface LectureCrawlingService {
	List<LectureInfo> getLectureList(Elements lectureElements, String lectureWeekId);
}
