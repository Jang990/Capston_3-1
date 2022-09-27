package com.esummary.entity.subject;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "subjectInfo")
public class TaskInfo {
	@Id
	private String taskId;
	private String title;
	@Column(length = 1000)
	private String description;

	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	private int submissionNum;
	private int notSubmittedNum;
	private int totalNum;
	
	@Transient
	private String submitYN; //이건 유저 테이블로 이동할 것.
	
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID")
	private SubjectInfo subjectInfo;

	public TaskInfo(String taskId, String title, String description, Date startDate, Date endDate,
			int submissionNum, int notSubmittedNum, int totalNum, String submitYN, String subjectId) {
		super();
		this.taskId = taskId;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.submissionNum = submissionNum;
		this.notSubmittedNum = notSubmittedNum;
		this.totalNum = totalNum;
		this.submitYN = submitYN;
		this.subjectInfo = new SubjectInfo();
		this.subjectInfo.setSubjectId(subjectId);
	}
	
}
