package com.esummary.elearning.dto.subject;

import java.util.List;

import lombok.Data;

@Data
public class SubjectCountData {
	//완료 과제, 미완료 과제 
	private int cntCompletedTask;
	private int cntIncompletedTask;
	//수강강의, 미수강 강의
	private int cntCompletedLecture;
	private int cntIncompletedLecture;
	
	//과제 + 강의 토탈 수행 카운트
	private int cntCompletedTotal;
	private int cntIncompletedTotal;
	
	public SubjectCountData(List<LectureWeekData> lectureDTO, List<TaskData> taskDTO) {
		
		if(lectureDTO == null) {
			this.cntCompletedLecture = 0;
			this.cntIncompletedLecture = 0;
		} else {
			this.countLecture(lectureDTO);
		}
		
		if(taskDTO == null) {
			this.cntCompletedTask = 0;
			this.cntIncompletedTask = 0;
		} else {
			this.countTask(taskDTO);
		}
		
		this.cntCompletedTotal = this.cntCompletedLecture + this.cntCompletedTask;
		this.cntIncompletedTotal = this.cntIncompletedLecture + this.cntIncompletedTask;
	}

	private void countLecture(List<LectureWeekData> lectureDTO) {
		int cntCompletedLecture = 0;
		int cntIncompletedLecture = 0;
		for (LectureWeekData lectureWeekData : lectureDTO) {
			cntCompletedLecture += lectureWeekData.getCntCompleted();
			cntIncompletedLecture += lectureWeekData.getCntIncompleted();
		} 
		this.cntCompletedLecture = cntCompletedLecture;
		this.cntIncompletedLecture = cntIncompletedLecture;
	}   
	
	private void countTask(List<TaskData> taskDTO) {
		int cntCompletedTask = 0;       
		int cntIncompletedTask = 0; 
		for (TaskData taskData : taskDTO) {
			if(taskData.getSubmitYN().equals("Y")) cntIncompletedTask++;
			else if (taskData.getSubmitYN().equals("N")) cntCompletedTask++; 
		}
		this.cntCompletedTask = cntCompletedTask;
		this.cntIncompletedTask = cntIncompletedTask;
	}
	
}
