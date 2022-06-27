package com.esummary.elearning.service.subject.util.crawling.lectures.lecture;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.user.UserSubject;

public interface LectureUtil {
	List<SubjectLecture> getLectureList(Elements lectureElements, String lectureWeekId);
}
