package com.esummary.elearning.dto.subject;

import com.esummary.elearning.entity.subject.SubjectInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDTO {
	private String subjectId;
	private String subjectName;
	private String subjectOwnerName;
	private String openType;
	
	public SubjectDTO(SubjectInfo subject) {
		subjectId = subject.getSubjectId();
		subjectName = subject.getSubjectName();
		subjectOwnerName = subject.getSubjectOwnerName();
		openType = subject.getOpenType();
	}
}
