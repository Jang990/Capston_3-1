package com.esummary.crawling.service.crawling.task;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.TaskInfo;
import com.esummary.entity.user.UserInfo;
import com.esummary.entity.user.UserSubject;

@Component
public interface TaskCrawlingService {
	List<TaskInfo> getSubjectTaskInfo(String subjectId, Map<String, String> initialCookies);
}
