package com.esummary.elearning.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDetailData_VO {
	
	private List<LectureWeekData> lecture;
	private List<TaskData> task;
	private List<NoticeData> notice;
	private SubjectCountData subjectCounts;
	
}
