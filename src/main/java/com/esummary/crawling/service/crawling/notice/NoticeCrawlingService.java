package com.esummary.crawling.service.crawling.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.esummary.crawling.service.crawling.ELearningURL;
import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserSubject;

public interface NoticeCrawlingService {
	List<NoticeInfo> getSubjectNoticeInfo(String subjectId, Map<String, String> initialCookies);
}
