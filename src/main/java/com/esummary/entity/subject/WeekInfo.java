package com.esummary.entity.subject;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "subjectInfo")
public class WeekInfo {
	@Id
	private String weekId; 
	private String title;
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	
	
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID")
	private SubjectInfo subjectInfo;
	
	@OneToMany(mappedBy = "weekInfo")
	private List<LectureInfo> lectures;

	public WeekInfo(String weekId, String title, Date startDate, Date endDate,
			String subjectId, List<LectureInfo> lectures) {
		this.weekId = weekId;
		this.title = title;
		this.startDate = startDate;
		this.endDate = endDate;
		this.subjectInfo = new SubjectInfo();
		subjectInfo.setSubjectId(subjectId);
		this.lectures = lectures;
	}
	
	public String getSubjectId() {
		return this.subjectInfo.getSubjectId();
	}
	
}
