package com.esummary.elearning.service.vue;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.test.TestService;

@Service
public class VueServiceImpl implements VueService {
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired
	SubjectNoticeRepository subjectNoticeRepository;
	
	@Override
	public List<SubjectCardData> getInitCardData(String studentNumber) {
		UserInfo user = userRepository.findWithUserSubjectsByStudentNumber(studentNumber);
		List<SubjectCardData> cardList = new ArrayList<>();
		
		for (UserSubject userSubject : user.getUserSubjects()) {
			SubjectInfo subject = userSubject.getSubjectInfo();
			SubjectCardData card = new SubjectCardData(
					subject.getSubjectId(), subject.getSubjectName(), subject.getSubjectOwnerName());
			cardList.add(card);
		}
		
		if(cardList.size() <= 0) 
			return null;
		
		return cardList;
	}
	
	public List<NoticeData> getNoticeData(String subjectId) {
		//이게 오류 원인이다. 검색을하려면 꼭 객체가 있어야하나?
		List<SubjectNoticeInfo> noticeInfo = subjectNoticeRepository.findBySubjectInfo_SubjectId(subjectId);
		
		
		List<NoticeData> noticeData = new ArrayList<>();
		for (SubjectNoticeInfo subjectNoticeInfo : noticeInfo) {
			NoticeData notice = new NoticeData(subjectNoticeInfo.getTitle(), subjectNoticeInfo.getDescription());
			noticeData.add(notice);
		}
		if(noticeData.size() <= 0) return null;
		
		return noticeData;
	}

}
