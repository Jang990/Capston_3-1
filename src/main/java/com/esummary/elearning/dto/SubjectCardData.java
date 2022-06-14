package com.esummary.elearning.dto;

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
}
