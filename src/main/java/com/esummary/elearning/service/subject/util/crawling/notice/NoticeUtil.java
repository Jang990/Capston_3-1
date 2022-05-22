package com.esummary.elearning.service.subject.util.crawling.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.service.subject.ELearningServiceImpl;

public interface NoticeUtil {
	List<SubjectNoticeInfo> getSubjectNoticeInfo(UserSubject userSubject, Document docStudyHome, Map<String, String> initialCookies);
	List<SubjectNoticeInfo> getSubjectNoticeInfo(UserSubject userSubject, Map<String, String> initialCookies);
}
