package com.esummary.elearning.dao.notice;

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
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.exservice.crawling.ELearningURL;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.repository.subject.NoticeInfoRepository;
import com.esummary.elearning.repository.subject.TaskInfoRepository;

@Component
public class NoticeUtil_DB implements DBNoticeUtil{
	
	@Autowired
	private NoticeInfoRepository subjectNoticeRepository;
	
	public List<NoticeInfo> getSubjectNoticeInfo(SubjectInfo subjectInfo) {
		List<NoticeInfo> noticeList = null;
		noticeList = subjectNoticeRepository.findBySubjectInfo(subjectInfo);
		return noticeList;
	}
	
	@Override
	public boolean saveService(NoticeInfo notice) {
		if(validateDuplicate(notice))
			return false;
		
		subjectNoticeRepository.save(notice);
		return true;
	}

	@Override
	public boolean saveService(List<NoticeInfo> notices) {
		List<NoticeInfo> savedLectureWeeks = new ArrayList<NoticeInfo>();
		
		for (NoticeInfo notice : notices) {
			if(validateDuplicate(notice)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedLectureWeeks.add(notice);
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		subjectNoticeRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(NoticeInfo notice) {
		Optional<NoticeInfo> noticeCheck = subjectNoticeRepository.
				findByNoticeIdAndTitleAndDescription(notice.getNoticeId(), notice.getTitle(), notice.getDescription());
		
		if(noticeCheck.isEmpty()) return false;
		else return true; //중복 맞음
	}
	
}
