package com.esummary.elearning.service.dao.notice;

import java.util.ArrayList; 
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.service.crawling.ELearningURL;

@Component
public class NoticeUtil_DB implements DBNoticeUtil{
	
	@Autowired
	private SubjectNoticeRepository subjectNoticeRepository;
	
	public List<SubjectNoticeInfo> getSubjectNoticeInfo(SubjectInfo subjectInfo) {
		List<SubjectNoticeInfo> noticeList = null;
		noticeList = subjectNoticeRepository.findBySubjectInfo(subjectInfo);
		return noticeList;
	}
	
	@Override
	public boolean saveService(SubjectNoticeInfo notice) {
		if(validateDuplicate(notice))
			return false;
		
		subjectNoticeRepository.save(notice);
		return true;
	}

	@Override
	public boolean saveService(List<SubjectNoticeInfo> notices) {
		List<SubjectNoticeInfo> savedLectureWeeks = new ArrayList<SubjectNoticeInfo>();
		
		for (SubjectNoticeInfo notice : notices) {
			if(validateDuplicate(notice)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedLectureWeeks.add(notice);
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		subjectNoticeRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(SubjectNoticeInfo notice) {
		Optional<SubjectNoticeInfo> noticeCheck = subjectNoticeRepository.
				findByNoticeIdAndTitleAndDescription(notice.getNoticeId(), notice.getTitle(), notice.getDescription());
		
		if(noticeCheck.isEmpty()) return false;
		else return true; //중복 맞음
	}
	
}
