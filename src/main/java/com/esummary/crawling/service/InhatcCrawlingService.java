package com.esummary.crawling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.esummary.auth.exception.DeniedElearningCookieException;
import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.InhatcUserDTO;
import com.esummary.crawling.dto.exInitalPageData;
import com.esummary.crawling.dto.exSubjectCardData;
import com.esummary.elearning.dao.DBSubjectUtil;
import com.esummary.elearning.dao.DBUserSubjectUtil;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.exservice.crawling.SubjectCrawlingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InhatcCrawlingService implements CrawlingService {
	
	private final SubjectCrawlingService subjectUtil;
	private final DBSubjectUtil dbSubjectUtil;
	private final DBUserSubjectUtil dbUserSubjectUtil;
	
	@Override
	public List<InhatcSubjectCardDTO> crawlLoginPage(InhatcUserDTO userDTO) {
		
		//크롤링 정보 가져오기
		List<SubjectInfo> basicSubjectData = subjectUtil.crawlBasicSubjectInfo(userDTO.getInitialCookies());
		
		System.out.println("===========>ㅇㅇㅇ");
		for (SubjectInfo subjectInfo : basicSubjectData) {
			System.out.println(subjectInfo.getSubjectId() + "," + subjectInfo.getSubjectName());
		}
		
		if(basicSubjectData.isEmpty() || basicSubjectData == null) 
			throw new DeniedElearningCookieException("만료된 이러닝 로그인 쿠키"); // 크롤링 실패
		
		//UserSubject와 Subject 저장
		UserInfo userEntity = InhatcUserDTO.toEntity(userDTO);
		dbSubjectUtil.saveService(basicSubjectData);
		List<UserSubject> usList = new ArrayList<UserSubject>();
		for (SubjectInfo subjectInfo : basicSubjectData) {
			usList.add(new UserSubject(userEntity, subjectInfo));
		}
		dbUserSubjectUtil.saveService(usList);
		
		List<InhatcSubjectCardDTO> subjectCards = new ArrayList<>();
		for (SubjectInfo subject : basicSubjectData) {
			subjectCards.add(InhatcSubjectCardDTO.from(subject));
		}
		
		return subjectCards;
	}

}
