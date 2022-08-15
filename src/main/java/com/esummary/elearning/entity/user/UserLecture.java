package com.esummary.elearning.entity.user;
 
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLecture {
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ulId;
	private String status; // 0 or 1 //학습 완료: 1, 학습 미완료: 0 
	private String learningTime;
	
	@ManyToOne
	@JoinColumn(name = "US_ID")
	private UserSubject userSubject;
	
	@ManyToOne
	@JoinColumn(name = "LECTURE_ID")
	private LectureInfo subjectLecture;
	
	public long getSubjectLectureId() {
		return this.subjectLecture.getLectureId();
	}
	
	public long getUserSubjectId() {
		return this.userSubject.getUsId();
	}

	public UserLecture(LectureInfo lecture, UserSubject userSubject) {
//		this.ulId = ulId;
		this.status = lecture.getStatus();
		this.learningTime = lecture.getLearningTime();
		this.userSubject = userSubject;
		this.subjectLecture = lecture;
	}
	
	
}
