package com.esummary.elearning.service.subject.util.db.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;

@Component
public class NoticeUtil_DB implements DBNoticeUtil{
	
	@Autowired
	private SubjectNoticeRepository subjectNoticeRepository;
	
	public List<SubjectNoticeInfo> getSubjectNoticeInfo(SubjectInfo subjectInfo) {
		List<SubjectNoticeInfo> noticeList = null;
		noticeList = subjectNoticeRepository.findBySubjectInfo(subjectInfo);
		return noticeList;
	}
	
}
