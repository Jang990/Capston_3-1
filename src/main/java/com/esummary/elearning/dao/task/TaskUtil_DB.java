package com.esummary.elearning.dao.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.subject.TaskInfoRepository;
import com.esummary.elearning.repository.user.UserTaskRepository;
import com.esummary.elearning.service.crawling.ELearningURL;
import com.esummary.elearning.service.crawling.SubjectCrawlingService_Inhatc;

@Component
public class TaskUtil_DB implements DBTaskUtil{
	
	@Autowired
	private TaskInfoRepository subjectTaskRepository;
	@Autowired
	private UserTaskRepository userTaskRepository;
	
	
	public List<TaskInfo> getSubjectTaskInfo(SubjectInfo subjectInfo) {
		List<TaskInfo> taskList = null;
		taskList = subjectTaskRepository.findBySubjectInfo(subjectInfo);
		
		return taskList;
	}


	@Override
	public List<UserTask> getUserTask(List<TaskInfo> taskList) {
		List<UserTask> userTaskList = new ArrayList<UserTask>();
		for (TaskInfo subjectTaskInfo : taskList) {
			Optional<UserTask> check = userTaskRepository.findByTaskInfo(subjectTaskInfo);
			if(check.isEmpty()) continue;
			UserTask ut = check.get();
			ut.setTaskInfo(subjectTaskInfo);
			userTaskList.add(ut);
		}
		
		return userTaskList;
	}


	@Override
	public boolean saveService(TaskInfo task) {
		if(validateDuplicate(task))
			return false;
		
		subjectTaskRepository.save(task);
		return true;
	}


	@Override
	public boolean saveService(List<TaskInfo> tasks) {
		List<TaskInfo> savedTasks = new ArrayList<TaskInfo>();
		
		for (TaskInfo task : tasks) {
			if(validateDuplicate(task)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedTasks.add(task);
		}
		
		if(savedTasks.size() == 0) return false;
		
		subjectTaskRepository.saveAll(savedTasks);
		return true;
	}


	@Override
	public boolean validateDuplicate(TaskInfo task) {
		//값이 중복되는지도 체크한다.
		Optional<TaskInfo> taskCheck = subjectTaskRepository.
				findByTaskId(task.getTaskId());
		
		if(taskCheck.isEmpty() || !equalEntityValue(task, taskCheck.get())) return false;
		else return true; //중복 맞음
	}
	
	private boolean equalEntityValue(TaskInfo task1, TaskInfo task2) {
		if(
				task1.getSubmitYN().equals(task2.getSubmitYN()) &&
				task1.getDescription().equals(task2.getDescription()) &&
				task1.getEndDate().equals(task2.getEndDate()) &&
				task1.getStartDate().equals(task2.getStartDate()) &&
				task1.getTitle().equals(task2.getTitle()) && 
				task1.getNotSubmittedNum() == task2.getNotSubmittedNum()
			)
			return true; // 실제 값이 같음.
		else
			return false;
	}
	
}
