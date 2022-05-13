package com.esummary.elearning.service.subject.util.crawling.task;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;

@Component
public interface TaskUtil {
//	List<SubjectTaskInfo> getSubjectTaskInfo(UserSubject userSubject, Document docStudyHome, SubjectInfo subjectInfo);
	List<SubjectTaskInfo> getSubjectTaskInfo(UserSubject userSubject, Document docStudyHome, Map<String, String> initialCookies);
}
