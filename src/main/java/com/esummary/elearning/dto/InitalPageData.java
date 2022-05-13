package com.esummary.elearning.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitalPageData {
	String name;
	String studentNumber;
	List<String> subjectName;
}
