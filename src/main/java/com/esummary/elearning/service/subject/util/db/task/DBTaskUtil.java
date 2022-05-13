package com.esummary.elearning.service.subject.util.db.task;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;

@Component
public interface DBTaskUtil {
//	List<SubjectTaskInfo> getSubjectTaskInfo(UserSubject userSubject, Document docStudyHome, SubjectInfo subjectInfo);
	List<SubjectTaskInfo> getSubjectTaskInfo(SubjectInfo subjectInfo);

	List<UserTask> getUserTask(List<SubjectTaskInfo> taskList);
}
