package com.esummary.elearning.entity.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.TaskInfo;

import lombok.Data;

@Entity
@Data
public class UserTask {
	@Id
	private Long utId;
	private String submitYN;
//	private String taskId;
	
	@ManyToOne
	@JoinColumn(name = "US_ID")
	private UserSubject userSubject;
	
	@ManyToOne
	@JoinColumn(name = "TASK_ID")
	private TaskInfo taskInfo;
	
	public UserTask() {
		
	}
	
	public UserTask(TaskInfo task, UserSubject userSubject) {
		this.submitYN = task.getSubmitYN();
		this.userSubject = userSubject;
		this.taskInfo = task;
	}
	
	public String getSubjectTaskId() {
		return this.taskInfo.getTaskId();
	}
	
	public long getUserSubjectId() {
		return this.userSubject.getUsId();
	}

	public UserTask(Long utId, String submitYN, UserSubject userSubject, TaskInfo subjectTaskInfo) {
		super();
		this.utId = utId;
		this.submitYN = submitYN;
		this.userSubject = userSubject;
		this.taskInfo = subjectTaskInfo;
	}
	
}
