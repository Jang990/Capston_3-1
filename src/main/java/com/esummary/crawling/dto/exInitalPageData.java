package com.esummary.crawling.dto;

import java.util.ArrayList;
import java.util.List;

import com.esummary.elearning.entity.subject.SubjectInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class exInitalPageData {
	String studentNumber;
	List<exSubjectCardData> subjectCardData;  

	public exInitalPageData(String name, String studentNumber, List<exSubjectCardData> subjectCardData) {
		this.studentNumber = studentNumber;
		this.subjectCardData = subjectCardData;
	}
	
	public exInitalPageData(List<SubjectInfo> subjects, String name, String studentNumber) {
		this.studentNumber = studentNumber;
		this.subjectCardData = getSubjectDTO(subjects);
	}
	
	private List<exSubjectCardData> getSubjectDTO(List<SubjectInfo> subjects) {
		List<exSubjectCardData> subjectCards = new ArrayList<exSubjectCardData>();
		for (SubjectInfo subjectInfo : subjects) {
			subjectCards.add(new exSubjectCardData(subjectInfo));
		}
		return subjectCards;
	}
}
