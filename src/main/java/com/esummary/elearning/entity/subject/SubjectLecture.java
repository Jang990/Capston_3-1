package com.esummary.elearning.entity.subject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class SubjectLecture {
	@Id
	Long lectureId;
	String lectureVideoId;//id로 사용하기 적합하지 않음. 없을 때가 있다.
	String type;
	String idx;
	String title;
	String fullTime;
//	String jsCode;
	
	@ManyToOne
	@JoinColumn(name = "LECTURE_WEEK_ID")
//	@Transient
	private SubjectLectureWeekInfo subjectLectureWeekInfo;

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

	public SubjectLectureWeekInfo getSubjectLectureWeekInfo() {
		return subjectLectureWeekInfo;
	}

	public void setSubjectLectureWeekInfo(SubjectLectureWeekInfo subjectLectureWeekInfo) {
		this.subjectLectureWeekInfo = subjectLectureWeekInfo;
	}
	
	
}
