package com.esummary.crawling.dto.tofront;

import java.util.List;

import com.esummary.crawling.dto.SubjectCountData;

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
