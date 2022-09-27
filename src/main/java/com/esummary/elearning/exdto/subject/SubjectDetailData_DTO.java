package com.esummary.elearning.exdto.subject;

import java.util.List;

import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.TaskInfo;
import com.esummary.entity.subject.WeekInfo;

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
