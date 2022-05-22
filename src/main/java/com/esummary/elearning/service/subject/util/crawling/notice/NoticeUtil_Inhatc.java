package com.esummary.elearning.service.subject.util.crawling.notice;

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
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil_Inhatc;

//@Component
@Component("crawlNot")
public class NoticeUtil_Inhatc implements NoticeUtil{
	
	private static int cnt = 1;
	
	@Autowired
	private SubjectNoticeRepository subjectNoticeRepository;
	
	public List<SubjectNoticeInfo> getSubjectNoticeInfo(UserSubject userSubject, Document docStudyHome, Map<String, String> initialCookies) {
		List<SubjectNoticeInfo> noticeList = new ArrayList<SubjectNoticeInfo>();
		//StudyHome에서 과제 내용이 적혀있는 섹션에 css Selector
		final String noticePageSelector = "#1 > ul > li:nth-child(1) > a";
		
		final String noticeBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";
		Document docNoticePage = ELearningServiceImpl.gotoHrefPageFromHomePage(initialCookies, docStudyHome, noticePageSelector);
		Elements notices = docNoticePage.select(noticeBoxSelector);
		
		for (Element element : notices) {
			SubjectNoticeInfo notice = createNotice(element, userSubject.getSubjectInfo());
			if(notice != null) {
				noticeList.add(notice);
			}
		}
		
		return noticeList;
		/*
		 * 전부 불러오기까지 성공
		 * 그러나 다른 페이지가 있을 때 페이지 이동을 해서 파싱하는 것은 구현 못함.
		 * 발명과 특허 과목 1, 2페이지 파싱하기.
		 * 만약 페이지가 1 밖에 없다면 신경쓰지 않도록 만들기.
		 */
	}
	
	private SubjectNoticeInfo createNotice(Element element, SubjectInfo subjectInfo) {
		//id가 없다면 무조건 저장
		//title과 desc가 바꼈다면 업데이트
		//이것도 아니라면 null
		//JPA에 save()원리 알아보기. 자기가 알아서 insert할지 update할지 정하는듯. 그러면 이렇게 플래그 세울필요없다.
		
		String id = crawlNoticeId(element);
		String title = crawlTitle(element);
		if(title.equals("") || title == null)
			return null; //크롤링오류임
		String description = crawlDescription(element);

		SubjectNoticeInfo existNotice = subjectNoticeRepository
				.findByNoticeIdAndTitleAndDescription(id, title, description);
		if(existNotice != null) {
			System.out.println("여기 실행 카운트:" + cnt++); 
			return existNotice; //크롤링할 필요 없음
		}
		
		String author = crawlAutor(element);
		String createDate = crawlCreateDate(element);
		
		SubjectNoticeInfo createdNotice = 
				new SubjectNoticeInfo(id, title, author, createDate, description, subjectInfo);
		subjectNoticeRepository.save(createdNotice);
		
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
		String id = SubjectUtil_Inhatc.extractDataFromJsCode(idJSCode)[0];
		return id;
	}

	@Override
	public List<SubjectNoticeInfo> getSubjectNoticeInfo(UserSubject userSubject, Map<String, String> initialCookies) {
		List<SubjectNoticeInfo> noticeList = new ArrayList<SubjectNoticeInfo>();
		String studyHome = SubjectUtil_Inhatc.createStudyHomeUrl(userSubject.getSubjectId());
		Document docStudyHome = null;
		try {
			docStudyHome = Jsoup.connect(studyHome).cookies(initialCookies).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//StudyHome에서 과제 내용이 적혀있는 섹션에 css Selector
		final String noticePageSelector = "#1 > ul > li:nth-child(1) > a";
		final String noticeBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";
		Document docNoticePage = ELearningServiceImpl.gotoHrefPageFromHomePage(initialCookies, docStudyHome, noticePageSelector);
		Elements notices = docNoticePage.select(noticeBoxSelector);
		
		for (Element element : notices) {
			SubjectNoticeInfo notice = createNotice(element, userSubject.getSubjectInfo());
			if(notice != null) {
				noticeList.add(notice);
			}
		}
		
		return noticeList;
	}
}
