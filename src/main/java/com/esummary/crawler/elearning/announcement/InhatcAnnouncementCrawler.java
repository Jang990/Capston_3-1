package com.esummary.crawler.elearning.announcement;

import com.esummary.crawler.connection.PageConnector;
import com.esummary.crawler.connection.dto.ConnectionData;
import com.esummary.crawler.elearning.announcement.dto.AnnouncementDTO;
import com.esummary.crawler.elearning.dto.ContentDetail;
import com.esummary.crawler.elearning.util.InhatcUtil;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InhatcAnnouncementCrawler implements AnnouncementCrawler {

    private final PageConnector pageConnector;

    @Override
    public List<AnnouncementDTO> crawlAnnouncement(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        List<AnnouncementDTO> announcementList = new ArrayList<>();
        Elements announcements = crawlAnnouncementBox(courseId, loginSessionCookie);

        for (Element element : announcements) {
            if(!InhatcUtil.isContent(element))
                continue;

            AnnouncementDTO announcement = createAnnouncement(element, courseId);
            announcementList.add(announcement);
        }

        return announcementList;
    }

    private Elements crawlAnnouncementBox(String courseId, Map<String, String> loginCookies) throws IOException {
        final String announcementBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";
        Document announcementPage = getAnnouncementPage(courseId, loginCookies);
        return announcementPage.select(announcementBoxSelector);
    }

    private Document getAnnouncementPage(String courseId, Map<String, String> loginCookies) throws IOException {
        final String AnnouncementPageURLFormat = "https://cyber.inhatc.ac.kr/Course.do?cmd=viewBoardContentsList&boardInfoDTO.boardInfoGubun=notice&boardInfoDTO.boardInfoId=%s-N&boardInfoDTO.boardClass=notice&boardInfoDTO.boardType=course&courseDTO.courseId=%s&mainDTO.parentMenuId=menu_00048&mainDTO.menuId=menu_00056";
        String announcementPageURL = String.format(AnnouncementPageURLFormat, courseId, courseId);
        ConnectionData connectionData = new ConnectionData(announcementPageURL,loginCookies);
        return pageConnector.getContent(connectionData);
    }

    private AnnouncementDTO createAnnouncement(Element element, String subjectId) {

        ContentDetail contentDetail = getContentDetail(element);
        LocalDateTime date = getAnnouncementDate(element);
//        String author = crawlAuthor(element);
        AnnouncementDTO createdAnnouncement = new AnnouncementDTO(contentDetail, date);


        return createdAnnouncement;
    }

    private LocalDateTime getAnnouncementDate(Element element) {
        String date = crawlCreateDate(element);
        return InhatcUtil.parseDate(date);
    }

    private ContentDetail getContentDetail(Element element) {
        String id = crawlAnnouncementId(element);
        String title = crawlTitle(element);
        if(title.equals("") || title == null)
            return null; //크롤링오류임
        String content = crawlContent(element);

        return new ContentDetail(id,title,content);
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
        String id = InhatcUtil.extractDataFromJsCode(idJSCode)[0];
        return id;
    }
}
