package com.esummary.elearning.service.crawling;
 
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
import com.esummary.elearning.dto.user.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;

@Service
public class ELearningURL {
	public static final String MAIN_URL = "https://cyber.inhatc.ac.kr";
	
//	@Autowired
//	@Qualifier("DB")
//	private SubjectUtil subjectUtil_DB;
	
	public static Document gotoHrefPageFromHomePage(Map<String, String> initialCookies, Document docHomePage, String hrefPageSelector) {
		String pageUrl = docHomePage.select(hrefPageSelector).attr("href");
		try {
			return Jsoup.connect(ELearningURL.MAIN_URL + pageUrl).cookies(initialCookies).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
