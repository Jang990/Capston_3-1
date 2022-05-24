package com.esummary.elearning.service.subject;
 
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil;

@Service
public class ELearningServiceImpl implements ELearningService {
	public static final String MAIN_URL = "https://cyber.inhatc.ac.kr";
	
	@Autowired
	@Qualifier("Crawl")
	private SubjectUtil subjectUtil_Crawl;
	
//	@Autowired
//	@Qualifier("DB")
//	private SubjectUtil subjectUtil_DB;
	
	@Override
	public List<SubjectInfo> summary(UserInfo user) {
		//쓰레드 처리?
		return subjectUtil_Crawl.getSubjectList(user);
	}

	public static Document gotoHrefPageFromHomePage(Map<String, String> initialCookies, Document docHomePage, String hrefPageSelector) {
		String pageUrl = docHomePage.select(hrefPageSelector).attr("href");
		try {
			return Jsoup.connect(ELearningServiceImpl.MAIN_URL + pageUrl).cookies(initialCookies).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<SubjectInfo> crawlBasicSubjectData(UserData user) {
		UserInfo userInfo = createUserInfo(user);
//		userInfo.setStudentNumber(user.getStudentNumber());
//		userInfo.setUserName(user.getUserName());
//		userInfo.setInitialCookies(user.getInitialCookies());
		//크롤링
		List<SubjectInfo> subjects = subjectUtil_Crawl.crawlBasicSubject(userInfo);
	
		return subjects;
	}
	
	@Override
	public boolean saveBasicSubjectData(UserData user, List<SubjectInfo> subjects) {
		UserInfo userInfo = createUserInfo(user);
		//db에 내용 저장
		return subjectUtil_Crawl.saveBasicSubject(userInfo, subjects);
	}
	
	@Override
	public List<SubjectCardData> getSubjectDTO(List<SubjectInfo> subjects) {
		List<SubjectCardData> subjectCards = new ArrayList<SubjectCardData>();
		for (SubjectInfo subjectInfo : subjects) {
			subjectCards.add(new SubjectCardData(
					subjectInfo.getSubjectId(), 
					subjectInfo.getSubjectName(), 
					subjectInfo.getSubjectOwnerName()
				)
			);
		}
	
		return subjectCards;
	}
	
	private UserInfo createUserInfo(UserData user) {
		return new UserInfo(
				user.getStudentNumber(), 
				user.getUserName(), 
				null, 
				user.getInitialCookies(), 
				null
			);
	}
	
}
