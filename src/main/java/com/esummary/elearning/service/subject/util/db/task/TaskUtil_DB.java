package com.esummary.elearning.service.subject.util.db.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.repository.user.UserTaskRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil_Inhatc;

@Component
public class TaskUtil_DB implements DBTaskUtil{
	
	private static Long seq_UserTask = 1L;
	
	@Autowired
	private SubjectTaskRepository subjectTaskRepository;
	@Autowired
	private UserTaskRepository userTaskRepository;
	
	
	public List<SubjectTaskInfo> getSubjectTaskInfo(SubjectInfo subjectInfo) {
		List<SubjectTaskInfo> taskList = null;
		taskList = subjectTaskRepository.findBySubjectInfo(subjectInfo);
		
		return taskList;
	}


	@Override
	public List<UserTask> getUserTask(List<SubjectTaskInfo> taskList) {
		List<UserTask> userTaskList = new ArrayList<UserTask>();
		for (SubjectTaskInfo subjectTaskInfo : taskList) {
			UserTask ut = userTaskRepository.findBySubjectTaskInfo(subjectTaskInfo);
			ut.setSubjectTaskInfo(subjectTaskInfo);
			userTaskList.add(ut);
		}
		
		return userTaskList;
	}
	
}
