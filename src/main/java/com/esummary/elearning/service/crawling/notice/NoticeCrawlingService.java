package com.esummary.elearning.service.crawling.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.service.crawling.ELearningURL;

public interface NoticeCrawlingService {
	List<SubjectNoticeInfo> getSubjectNoticeInfo(String subjectId, Map<String, String> initialCookies);
}
