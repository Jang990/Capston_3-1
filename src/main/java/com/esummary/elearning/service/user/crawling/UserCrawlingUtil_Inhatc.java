package com.esummary.elearning.service.user.crawling;

import java.io.IOException; 
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;

@Component
public class UserCrawlingUtil_Inhatc implements UserCrawlingUtil{
	public final String MAIN_URL = "https://cyber.inhatc.ac.kr";
	
	public String getUserName(Map<String, String> loginCookies) {
		Document loginPage = conLoginPage(loginCookies);
		if(loginPage == null) return null;
		String name = crawlName(loginPage);
		return name;
	}

	private String crawlName(Document loginPage) {
		String[] nameAndWStudentNumber = loginPage.getElementsByClass("login_info").select("ul li").last().text().split(" ");
		return nameAndWStudentNumber[0].trim();
	}

	private Document conLoginPage(Map<String, String> initialCookies) {
		String mainUrl = "https://cyber.inhatc.ac.kr" + "/MMain.do";
		Document loginPage = null;
		
		try {
			Connection con = Jsoup.connect(mainUrl)
					.data("cmd", "viewIndexPage")
					.cookies(initialCookies);
			Connection.Response resp = con.execute();
			
			if(resp.statusCode() == 200)
				loginPage = con.post();
			else
				return null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return loginPage;
	}
	
	
}
