package com.esummary.elearning.entity.subject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "subjectInfo")
public class NoticeInfo {
	@Id
	private String noticeId; 
	private String title;
	private String author;
	private String createDate;
	@Column(length = 2000)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "SUBJECT_ID")
	private SubjectInfo subjectInfo;
	
	
	
	public NoticeInfo(String noticeId, String title, String author, String createDate, String description,
			String subjectId) {
		this.noticeId = noticeId;
		this.title = title;
		this.author = author;
		this.createDate = createDate;
		this.description = description;
		this.subjectInfo = new SubjectInfo();
		this.subjectInfo.setSubjectId(subjectId);
	}
}
