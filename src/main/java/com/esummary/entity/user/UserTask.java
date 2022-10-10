package com.esummary.entity.user;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.TaskInfo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@ToString(exclude = {"userSubject", "taskInfo"})
public class UserTask {
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
