package com.esummary.elearning.exservice.crawling.notice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.exservice.crawling.ELearningURL;
import com.esummary.elearning.exservice.crawling.SubjectCrawlingService_Inhatc;
import com.esummary.elearning.repository.subject.NoticeInfoRepository;
import com.esummary.elearning.repository.subject.TaskInfoRepository;

//@Component
@Component("crawlNot")
public class NoticeCrawlingService_Inhatc implements NoticeCrawlingService{

	@Override
	public List<NoticeInfo> getSubjectNoticeInfo(String subjectId, Map<String, String> loginCookies) {
		List<NoticeInfo> noticeList = new ArrayList<NoticeInfo>();
		Elements notices = crawlNoticeBox(subjectId, loginCookies);
		
		for (Element element : notices) {
			NoticeInfo notice = createNotice(element, subjectId);
			if(notice != null) {
				noticeList.add(notice);
			}
		}
		
		return noticeList;
	}
	
	private Elements crawlNoticeBox(String subjectId, Map<String, String> loginCookies) {
		Document docStudyHome = SubjectCrawlingService_Inhatc.connStudyHome(subjectId,loginCookies);
		
		//StudyHome에서 과제 내용이 적혀있는 섹션에 css Selector
		final String noticePageSelector = "#1 > ul > li:nth-child(1) > a";
		final String noticeBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";
		Document docNoticePage = ELearningURL.gotoHrefPageFromHomePage(loginCookies, docStudyHome, noticePageSelector);
		return docNoticePage.select(noticeBoxSelector);
	}

	private NoticeInfo createNotice(Element element, String subjectId) {
		String id = crawlNoticeId(element);
		String title = crawlTitle(element);
		if(title.equals("") || title == null)
			return null; //크롤링오류임
		String description = crawlDescription(element);
		String author = crawlAutor(element);
		String createDate = crawlCreateDate(element);
		
		NoticeInfo createdNotice = 
				new NoticeInfo(id, title, author, createDate, description, subjectId);
		
		return createdNotice; 
	}
	
	private String crawlCreateDate(Element element) {
		final String createDateSelector = "div > dl > dd.info > ul.fr.mr10 > li:nth-child(2)";
		String createDate = element.select(createDateSelector).text().replace("작성일 :", "").trim();
		return createDate;
	}

	private String crawlAutor(Element element) {
		final String authorSelector = "div > dl > dd.info > ul.fr.mr10 > li:nth-child(1) > span";
		String author = element.select(authorSelector).text().trim();
		return author;
	}

	private String crawlDescription(Element element) {
		final String descriptionSelector = "div > dl > dd:nth-child(3) > div";
		String description = element.select(descriptionSelector).text().trim();
		return description;
	}

	private String crawlTitle(Element element) {
		final String titleSelector = "div > dl > dt > h4 > a";
		String title = element.select(titleSelector).text().trim();
		return title;
	}

	private String crawlNoticeId(Element element) {
		final String idSelector = "div > dl > dt > ul > li > button";
		String idJSCode = element.select(idSelector).attr("onclick");
		String id = SubjectCrawlingService_Inhatc.extractDataFromJsCode(idJSCode)[0];
		return id;
	}

}
