package com.esummary.crawling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.esummary.crawling.dto.InhatcUserDTO;
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
	public boolean crawlLoginPage(InhatcUserDTO userDTO) {
//		Map<String, String> test = new HashMap<String, String>();
//		test.put("JSESSIONID", "aaa8hmdZh0xe5bBB7felym6HjzEl6Va_6lmpHrPKYr_hNaPoUjjo1hAEWyHD");
		
		//크롤링 정보 가져오기
		List<SubjectInfo> basicSubjectData = subjectUtil.crawlBasicSubjectInfo(userDTO.getInitialCookies());
//		List<SubjectInfo> basicSubjectData = subjectUtil.crawlBasicSubjectInfo(test);
		
		System.out.println("===========>ㅇㅇㅇ");
		for (SubjectInfo subjectInfo : basicSubjectData) {
			System.out.println(subjectInfo.getSubjectId() + "," + subjectInfo.getSubjectName());
		}
		
		if(basicSubjectData.isEmpty() || basicSubjectData == null) return false; // 크롤링 실패
		
		//UserSubject와 Subject 저장
		UserInfo userEntity = InhatcUserDTO.toEntity(userDTO);
		dbSubjectUtil.saveService(basicSubjectData);
		List<UserSubject> usList = new ArrayList<UserSubject>();
		for (SubjectInfo subjectInfo : basicSubjectData) {
			usList.add(new UserSubject(userEntity, subjectInfo));
		}
		dbUserSubjectUtil.saveService(usList);
		
		return true;
	}

}
