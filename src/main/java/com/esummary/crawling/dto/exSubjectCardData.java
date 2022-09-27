package com.esummary.crawling.dto;

import com.esummary.entity.subject.SubjectInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class exSubjectCardData {
	private String subjectId;
	private String subjectName; 
	private String owner;
	
	public exSubjectCardData(SubjectInfo subject) {
		this.subjectId = subject.getSubjectId();
		this.subjectName = subject.getSubjectName();
		this.owner = subject.getSubjectOwnerName();
	}
	
}
