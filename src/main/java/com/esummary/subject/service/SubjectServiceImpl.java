package com.esummary.subject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.elearning.dto.subject.SubjectDTO;
import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.repository.subject.NoticeInfoRepository;
import com.esummary.repository.subject.SubjectInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

	private final SubjectInfoRepository subjectInfoRepository;
	private final NoticeInfoRepository noticeRepository;
	
	@Override
	public SubjectDTO getSubject(String subjectId) {
		SubjectInfo subject = subjectInfoRepository.findBySubjectId(subjectId).orElseThrow(
				() -> new IllegalArgumentException("해당 과목이 없습니다. id="+ subjectId));
		
		return new SubjectDTO(subject);
	}

	@Override
	public List<NoticeData> getNoticeData(String subjectId) {
		List<NoticeInfo> noticeInfo = noticeRepository.findBySubjectInfo_SubjectId(subjectId)
				.orElseThrow(() -> new IllegalArgumentException("해당 과목을 찾을 수 없습니다. 과목ID ="+subjectId));
		
		List<NoticeData> noticeDTO = new ArrayList<>();
		for (NoticeInfo subjectNoticeInfo : noticeInfo) {
			NoticeData notice = NoticeData.from(subjectNoticeInfo);
			noticeDTO.add(notice); 
		}
		
		return noticeDTO;
	}
	
}
