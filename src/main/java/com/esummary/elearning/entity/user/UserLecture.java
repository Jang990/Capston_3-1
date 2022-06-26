package com.esummary.elearning.entity.user;
 
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLecture {
	@Id
	long ulId;
	String status; // 0 or 1 //학습 완료: 1, 학습 미완료: 0 
	String learningTime;
	
	@ManyToOne
	@JoinColumn(name = "US_ID")
	private UserSubject userSubject;
	
	@ManyToOne
	@JoinColumn(name = "LECTURE_ID")
	private SubjectLecture subjectLecture;
	
	public long getSubjectLectureId() {
		return this.subjectLecture.getLectureId();
	}
	
	public long getUserSubjectId() {
		return this.userSubject.getUsId();
	}

	public UserLecture(SubjectLecture lecture, UserSubject userSubject) {
//		this.ulId = ulId;
		this.status = lecture.getStatus();
		this.learningTime = lecture.getLearningTime();
		this.userSubject = userSubject;
		this.subjectLecture = lecture;
	}
	
	
}
