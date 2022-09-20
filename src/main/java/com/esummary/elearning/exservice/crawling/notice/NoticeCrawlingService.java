package com.esummary.elearning.exservice.crawling.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.exservice.crawling.ELearningURL;

public interface NoticeCrawlingService {
	List<NoticeInfo> getSubjectNoticeInfo(String subjectId, Map<String, String> initialCookies);
}
