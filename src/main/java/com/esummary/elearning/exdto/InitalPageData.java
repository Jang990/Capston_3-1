package com.esummary.elearning.exdto;

import java.util.ArrayList;
import java.util.List;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.exdto.subject.SubjectCardData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@AllArgsConstructor
public class InitalPageData {
	String name;
	String studentNumber;
	List<SubjectCardData> subjectCardData;  

	public InitalPageData(String name, String studentNumber, List<SubjectCardData> subjectCardData) {
		this.name = name;
		this.studentNumber = studentNumber;
		this.subjectCardData = subjectCardData;
	}
	
	public InitalPageData(List<SubjectInfo> subjects, String name, String studentNumber) {
		this.name = name;
		this.studentNumber = studentNumber;
		this.subjectCardData = getSubjectDTO(subjects);
	}
	
	private List<SubjectCardData> getSubjectDTO(List<SubjectInfo> subjects) {
		List<SubjectCardData> subjectCards = new ArrayList<SubjectCardData>();
		for (SubjectInfo subjectInfo : subjects) {
			subjectCards.add(new SubjectCardData(subjectInfo));
		}
		return subjectCards;
	}
}
