package com.esummary.elearning.exservice.crawling.task;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;

@Component
public interface TaskCrawlingService {
	List<TaskInfo> getSubjectTaskInfo(String subjectId, Map<String, String> initialCookies);
}
