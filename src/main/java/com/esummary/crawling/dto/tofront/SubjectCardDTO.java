package com.esummary.crawling.dto.tofront;

import java.util.List;

import com.esummary.crawling.dto.InhatcSubjectCardDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubjectCardDTO {
	List<InhatcSubjectCardDTO> subjectCardData;
}
