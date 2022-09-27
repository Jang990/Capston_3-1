package com.esummary.entity.subject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Entity
@ToString(exclude = "weekInfo")
public class LectureInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	private WeekInfo weekInfo;
	
	
	public void setLectureIdForCrawlingObject(Long lectureId) {
		//크롤링을 한 객체는 MySQL에서 ID를 save하기전까지 부여할 수 없기 때문에 직접 넣어주어야한다.
		this.lectureId = lectureId;
	}
	
	public String getWeekId() {
		return this.weekInfo.getWeekId();
	}
	
	public Long getLectureId() {
		return lectureId;
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

	public WeekInfo getWeekInfo() {
		return weekInfo;
	}

	public void setWeekInfo(WeekInfo weekInfo) {
		this.weekInfo = weekInfo;
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
		this.weekInfo = new WeekInfo();
		weekInfo.setWeekId(weekId);
	}
	
	
}
