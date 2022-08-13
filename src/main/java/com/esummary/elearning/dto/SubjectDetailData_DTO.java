package com.esummary.elearning.dto;

import java.util.List;

import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.TaskInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDetailData_DTO {
	
	private String subjectId;
	private List<WeekInfo> lecture;
	private List<TaskInfo> task;
	private List<NoticeInfo> notice;
}
