package com.esummary.crawler.announcement;

import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawler.logincrawler.LoginCrawler;
import com.esummary.crawling.service.crawling.SubjectCrawlingService_Inhatc;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InhatcAnnouncementCrawler implements AnnouncementCrawler {
    private final String AnnouncementPageURLFormat = "https://cyber.inhatc.ac.kr/Course.do?cmd=viewBoardContentsList&boardInfoDTO.boardInfoGubun=notice&boardInfoDTO.boardInfoId=%s-N&boardInfoDTO.boardClass=notice&boardInfoDTO.boardType=course&courseDTO.courseId=%s&mainDTO.parentMenuId=menu_00048&mainDTO.menuId=menu_00056";
    private final String announcementBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";

    @Override
    public List<AnnouncementDTO> crawlAnnouncement(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        List<AnnouncementDTO> announcementList = new ArrayList<>();
        Elements announcements = crawlAnnouncementBox(courseId, loginSessionCookie);

        for (Element element : announcements) {
            AnnouncementDTO announcement = createAnnouncement(element, courseId);
            if(announcement != null) {
                announcementList.add(announcement);
            }
        }

        return announcementList;
    }



    private Elements crawlAnnouncementBox(String courseId, Map<String, String> loginCookies) throws IOException {
        Document announcementPage = getAnnouncementPage(courseId, loginCookies);
        return announcementPage.select(announcementBoxSelector);
    }

    private Document getAnnouncementPage(String courseId, Map<String, String> loginCookies) throws IOException {
        String AnnouncementPageURL = String.format(AnnouncementPageURLFormat, courseId, courseId);
        return Jsoup.connect(AnnouncementPageURL).cookies(loginCookies).get();
    }

    private AnnouncementDTO createAnnouncement(Element element, String subjectId) {
        String id = crawlAnnouncementId(element);
        String title = crawlTitle(element);
        if(title.equals("") || title == null)
            return null; //크롤링오류임
        String content = crawlContent(element);
        String author = crawlAuthor(element);
        String date = crawlCreateDate(element);

        AnnouncementDTO createdAnnouncement =
                AnnouncementDTO.builder()
                        .announcementId(id)
                        .author(author).title(title)
                        .announcementDate(date)
                        .content(content).build();


        return createdAnnouncement;
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
