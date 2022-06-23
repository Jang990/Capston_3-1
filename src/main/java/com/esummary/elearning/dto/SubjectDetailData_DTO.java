package com.esummary.elearning.dto;

import java.util.List;

import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDetailData_DTO {
	
	private String subjectId;
	private List<SubjectLectureWeekInfo> lecture;
	private List<SubjectTaskInfo> task;
	private List<SubjectNoticeInfo> notice;
}
