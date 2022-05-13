package com.esummary.elearning.entity.user;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;

import lombok.Data;

@Data
@Entity
public class UserSubject {
	/*
	@EmbeddedId
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
    	@JoinColumn(name = "STUDENT_NUMBER", referencedColumnName = "STUDENT_NUMBER"),
    	@JoinColumn(name = "SUBJECT_ID", referencedColumnName = "SUBJECT_ID")
//            @JoinColumn(name = "STUDENT_NUMBER"),
//            @JoinColumn(name = "SUBJECT_ID")
    })
	UserSubject_Id id;
	*/
	@Id
	int usId; // 시퀸스 사용할 것
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STUDENT_NUMBER")
	private UserInfo userInfo;

//	@JoinColumn(name = "school_id", referencedColumnName = "school_id")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SUBJECT_ID")
	private SubjectInfo subjectInfo;
	
	@OneToMany(mappedBy = "userSubject")
	private List<UserTask> userTask;
	
	@OneToMany(mappedBy = "userSubject")
	private List<UserLecture> userLecture;
	
	public String getStudentNumber() {
		return this.userInfo.getStudentNumber();
	}
	public String getSubjectId() {
		return this.subjectInfo.getSubjectId();
	}
	
}
