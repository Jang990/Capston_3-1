package com.esummary.crawler.announcementcrawler;

import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawling.service.crawling.ELearningURL;
import com.esummary.crawling.service.crawling.SubjectCrawlingService_Inhatc;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InhatcAnnouncementCrawler implements AnnouncementCrawler {
    @Override
    public List<AnnouncementDTO> crawlAnnouncement(String courseId, Map<String, String> loginSessionCookie) {
        List<AnnouncementDTO> announcementList = new ArrayList<AnnouncementDTO>();
        Elements notices = crawlNoticeBox(courseId, loginSessionCookie);

        for (Element element : notices) {
            AnnouncementDTO notice = createNotice(element, courseId);
            if(notice != null) {
                announcementList.add(notice);
            }
        }

        return announcementList;
    }

    private Elements crawlNoticeBox(String courseId, Map<String, String> loginCookies) {
        Document docStudyHome = SubjectCrawlingService_Inhatc.connStudyHome(courseId,loginCookies);

        //StudyHome에서 과제 내용이 적혀있는 섹션에 css Selector
        final String noticePageSelector = "#1 > ul > li:nth-child(1) > a";
        final String noticeBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";
        Document docNoticePage = ELearningURL.gotoHrefPageFromHomePage(loginCookies, docStudyHome, noticePageSelector);
        return docNoticePage.select(noticeBoxSelector);
    }

    private AnnouncementDTO createNotice(Element element, String subjectId) {
        String id = crawlAnnouncementId(element);
        String title = crawlTitle(element);
        if(title.equals("") || title == null)
            return null; //크롤링오류임
        String content = crawlContent(element);
        String author = crawlAuthor(element);
        String date = crawlCreateDate(element);

        AnnouncementDTO createdNotice =
                AnnouncementDTO.builder()
                        .announcementId(id)
                        .author(author).title(title)
                        .announcementDate(date)
                        .content(content).build();


        return createdNotice;
    }

    private String crawlCreateDate(Element element) {
        final String createDateSelector = "div > dl > dd.info > ul.fr.mr10 > li:nth-child(2)";
        String createDate = element.select(createDateSelector).text().replace("작성일 :", "").trim();
        return createDate;
    }

    private String crawlAuthor(Element element) {
        final String authorSelector = "div > dl > dd.info > ul.fr.mr10 > li:nth-child(1) > span";
        String author = element.select(authorSelector).text().trim();
        return author;
    }

    private String crawlContent(Element element) {
        final String descriptionSelector = "div > dl > dd:nth-child(3) > div";
        String description = element.select(descriptionSelector).text().trim();
        return description;
    }

    private String crawlTitle(Element element) {
        final String titleSelector = "div > dl > dt > h4 > a";
        String title = element.select(titleSelector).text().trim();
        return title;
    }

    private String crawlAnnouncementId(Element element) {
        final String idSelector = "div > dl > dt > ul > li > button";
        String idJSCode = element.select(idSelector).attr("onclick");
        String id = SubjectCrawlingService_Inhatc.extractDataFromJsCode(idJSCode)[0];
        return id;
    }
}
