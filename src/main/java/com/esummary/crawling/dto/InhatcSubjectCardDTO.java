package com.esummary.crawling.dto;

import com.esummary.entity.subject.SubjectInfo;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InhatcSubjectCardDTO {
	private String subjectId;
	private String subjectName; 
	private String owner;
	private String chatRoomID;
	
	public static InhatcSubjectCardDTO from(SubjectInfo subject) {
		if(subject == null) return null;
		
		return InhatcSubjectCardDTO.builder()
				.subjectId(subject.getSubjectId())
				.subjectName(subject.getSubjectName())
				.owner(subject.getSubjectOwnerName())
				.build();
	}
	
}