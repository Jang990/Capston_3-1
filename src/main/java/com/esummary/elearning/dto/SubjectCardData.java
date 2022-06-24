package com.esummary.elearning.dto;

import com.esummary.elearning.entity.subject.SubjectInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectCardData {
	private String subjectId;
	private String subjectName; 
	private String owner;
	
	public SubjectCardData(SubjectInfo subject) {
		this.subjectId = subject.getSubjectId();
		this.subjectName = subject.getSubjectName();
		this.owner = subject.getSubjectOwnerName();
	}
	
}
