package com.esummary.crawling.dto;

import com.esummary.elearning.entity.subject.SubjectInfo;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InhatcSubjectCardDTO {
	private String subjectId;
	private String subjectName; 
	private String owner;
	
	public static InhatcSubjectCardDTO from(SubjectInfo subject) {
		if(subject == null) return null;
		
		return InhatcSubjectCardDTO.builder()
				.subjectId(subject.getSubjectId())
				.subjectName(subject.getSubjectName())
				.owner(subject.getSubjectOwnerName())
				.build();
	}
	
}