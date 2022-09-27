package com.esummary.subject.service;

import org.springframework.stereotype.Service;

import com.esummary.elearning.dto.subject.SubjectDTO;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.repository.subject.SubjectInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

	private final SubjectInfoRepository subjectInfoRepository;
	
	@Override
	public SubjectDTO getSubject(String subjectId) {
		SubjectInfo subject = subjectInfoRepository.findBySubjectId(subjectId).orElseThrow(
				() -> new IllegalArgumentException("해당 과목이 없습니다. id="+ subjectId));
		
		return new SubjectDTO(subject);
	}

}
