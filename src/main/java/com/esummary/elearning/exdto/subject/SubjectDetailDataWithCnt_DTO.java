package com.esummary.elearning.exdto.subject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDetailDataWithCnt_DTO {
	
	private List<LectureWeekData> lecture;
	private List<TaskData> task;
	private List<NoticeData> notice;
	private SubjectCountData subjectCounts;
	
}
