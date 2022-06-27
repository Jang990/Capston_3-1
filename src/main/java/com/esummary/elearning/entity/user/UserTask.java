package com.esummary.elearning.entity.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;

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
	private SubjectTaskInfo subjectTaskInfo;
	
	public UserTask() {
		
	}
	
	public UserTask(SubjectTaskInfo task, UserSubject userSubject) {
		this.submitYN = task.getSubmitYN();
		this.userSubject = userSubject;
		this.subjectTaskInfo = task;
	}
	
	public String getSubjectTaskId() {
		return this.subjectTaskInfo.getTaskId();
	}
	
	public long getUserSubjectId() {
		return this.userSubject.getUsId();
	}

	public UserTask(Long utId, String submitYN, UserSubject userSubject, SubjectTaskInfo subjectTaskInfo) {
		super();
		this.utId = utId;
		this.submitYN = submitYN;
		this.userSubject = userSubject;
		this.subjectTaskInfo = subjectTaskInfo;
	}
	
}
