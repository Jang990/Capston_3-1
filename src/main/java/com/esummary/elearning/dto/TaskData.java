package com.esummary.elearning.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.esummary.elearning.entity.subject.SubjectInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskData {
	private String taskId;
	private String title;
	private String description;
	private Date startDate;
	private Date endDate;
	private int submissionNum;
	private int notSubmittedNum;
	private int totalNum;
	private String submitYN;
}
