package com.esummary.elearning.exdto.subject;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserTask;

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
	private String startDate;
	private String endDate;
	private int submissionNum;
	private int notSubmittedNum;
	private int totalNum;
	private String submitYN;
	
	public static TaskData convertTaskData(TaskInfo subjectTaskInfo) {
		String startDate =makeDateString(subjectTaskInfo.getStartDate());
		String endDate =makeDateString(subjectTaskInfo.getEndDate());
		
		return new TaskData(
				subjectTaskInfo.getTaskId(),
				subjectTaskInfo.getTitle(),
				subjectTaskInfo.getDescription(),
				startDate,
				endDate,
				subjectTaskInfo.getSubmissionNum(),
				subjectTaskInfo.getNotSubmittedNum(),
				subjectTaskInfo.getTotalNum(),
				subjectTaskInfo.getSubmitYN()
		);
	}
	
	public static TaskData convertTaskData(UserTask userTask) {
		TaskInfo task = userTask.getTaskInfo();
		String startDate = makeDateString(task.getStartDate());
		String endDate = makeDateString(task.getEndDate());
		return new TaskData(
				task.getTaskId(), task.getTitle(), task.getDescription(), 
				startDate, endDate, task.getSubmissionNum(), 
				task.getNotSubmittedNum(), task.getTotalNum(), task.getSubmitYN()
			);
	}
	
	private static String makeDateString(Date startDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DATE)
		+ " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
	}
}
