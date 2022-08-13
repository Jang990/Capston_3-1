package com.esummary.elearning.entity.subject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Entity
@ToString(exclude = "subjectLectureWeekInfo")
public class LectureInfo {
	@Id
	Long lectureId;
	String lectureVideoId;//id로 사용하기 적합하지 않음. 없을 때가 있다.
	String type;
	String idx;
	String title;
	String fullTime;
//	String jsCode;
	
	@Transient
	String status; // 0 or 1 //학습 완료: 1, 학습 미완료: 0 
	@Transient
	String learningTime;
	
	@ManyToOne
	@JoinColumn(name = "LECTURE_WEEK_ID")
//	@Transient
	private WeekInfo subjectLectureWeekInfo;

	
	public String getLectureWeekId() {
		return this.subjectLectureWeekInfo.getWeekId();
	}
	
	public Long getLectureId() {
		return lectureId;
	}

	public void setLectureId(Long lectureId) {
		this.lectureId = lectureId;
	}

	public String getLectureVideoId() {
		return lectureVideoId;
	}

	public void setLectureVideoId(String lectureVideoId) {
		this.lectureVideoId = lectureVideoId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdx() {
		return idx;
	}

	public void setIdx(String idx) {
		this.idx = idx;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFullTime() {
		return fullTime;
	}

	public void setFullTime(String fullTime) {
		this.fullTime = fullTime;
	}

	public WeekInfo getSubjectLectureWeekInfo() {
		return subjectLectureWeekInfo;
	}

	public void setSubjectLectureWeekInfo(WeekInfo subjectLectureWeekInfo) {
		this.subjectLectureWeekInfo = subjectLectureWeekInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLearningTime() {
		return learningTime;
	}

	public void setLearningTime(String learningTime) {
		this.learningTime = learningTime;
	}

	public LectureInfo(String lectureVideoId, String type, String idx, String title, String fullTime, String status,
			String learningTime, String weekId) {
		this.lectureVideoId = lectureVideoId;
		this.type = type;
		this.idx = idx;
		this.title = title;
		this.fullTime = fullTime;
		this.status = status;
		this.learningTime = learningTime;
		this.subjectLectureWeekInfo = new WeekInfo();
		subjectLectureWeekInfo.setWeekId(weekId);
	}
	
	
}
