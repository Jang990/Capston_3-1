package com.esummary.crawler.elearning.week;

import com.esummary.crawler.connection.PageConnector;
import com.esummary.crawler.connection.dto.ConnectionData;
import com.esummary.crawler.elearning.dto.ContentDetail;
import com.esummary.crawler.elearning.dto.ContentPeriod;
import com.esummary.crawler.elearning.week.lecture.dto.LectureDTO;
import com.esummary.crawler.elearning.week.dto.WeekDTO;
import com.esummary.crawler.elearning.util.InhatcUtil;
import com.esummary.crawler.elearning.week.lecture.InhatcLectureCrawler;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class InhatcWeekCrawler implements WeekCrawler {
    private final String Lecture_Page_Url_Format = "https://cyber.inhatc.ac.kr/Lesson.do?cmd=viewLessonContentsList&boardInfoDTO.boardInfoGubun=kyoanle&type=U&courseDTO.courseId=%s&mainDTO.parentMenuId=menu_00091&mainDTO.menuId=menu_00099";
    private final InhatcLectureCrawler lectureCrawler;
    private final PageConnector connector;

    @Override
    public List<WeekDTO> crawlLecture(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        Document lecturePage = getLecturePage(courseId, loginSessionCookie);
        Elements lectures = crawlLectureWeekContentsBox(lecturePage);

        //주차 정보 크롤링 - 이 하위에는 강의 차시 크롤링도 포함된다.
        List<WeekDTO> weekList = new ArrayList<>();
        for (Element element : lectures) {
            if(!InhatcUtil.isContent(element))
                continue;

            WeekDTO week = crawlWeekDetailInfo(element);
//            System.out.println("week = " + week);
            weekList.add(week);
        }

        return weekList;
    }

    private Document getLecturePage(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        String lecturePageUrl = String.format(Lecture_Page_Url_Format, courseId);
        ConnectionData connectionData = new ConnectionData(lecturePageUrl, loginSessionCookie);
        return connector.getContent(connectionData);
//        System.out.println("lecturePage = " + lecturePage);
    }

    private WeekDTO crawlWeekDetailInfo(Element element) {
        Elements lectureElements = crawlLectureBox(element);

        String titleAndDeadline = crawlTitleAndDaealine(element);
        Map<String, String> splitData = splitDataInTitleString(titleAndDeadline);

        String weekId = crawlWeekId(element);
        String title = splitData.get("title");
        ContentDetail contentDetail = new ContentDetail(weekId, title, null);

        LocalDateTime startDate = InhatcUtil.parseDate(splitData.get("startDate"));
        LocalDateTime endDate = InhatcUtil.parseDate(splitData.get("endDate"));
        ContentPeriod contentPeriod = new ContentPeriod(startDate, endDate);

        List<LectureDTO> lectureList = lectureCrawler.getLectureList(lectureElements);

        WeekDTO week = new WeekDTO(contentDetail, contentPeriod, lectureList);

        return week;
    }

    private String crawlWeekId(Element element) {
        final String idSelector = "div > dl > dt > h4 > span";
        String id = element.select(idSelector).attr("onclick")
                .replace("contentsInfoPop('", "")
                .replace("');return false;", "");
        return id;
    }

    private String crawlTitleAndDaealine(Element element) {
        final String titleAndDeadlineSelector = "div > dl > dt > h4";
        String titleAndDeadLine = element.select(titleAndDeadlineSelector).text();
        return titleAndDeadLine;
    }

    private Elements crawlLectureBox(Element element) {
        final String lecturesSelector = "div > dl > dd > div > table > tbody > tr";
        return element.select(lecturesSelector);
    }

    private Map<String, String> splitDataInTitleString(String titleAndDeadLine) {
		/*
		1주차 과제 2022-02-27 ~ 2022-06-17 학습목표보기
		이 데이터를
		학습목표보기를 없애고
		title / StartDate / EndDate로 나눈다.
		1주차 과제 / 2022-02-27 / 2022-06-17
		*/

        titleAndDeadLine = titleAndDeadLine.replace(" 학습목표보기", "");
        String[] splitData = titleAndDeadLine.split(" ");
        int tildeIdx = InhatcUtil.findTildeIdx(splitData);

        if(tildeIdx == 0) return null;

        String title = concatTitle(splitData, tildeIdx-1);
        String startDate = splitData[tildeIdx - 1].trim();
        String endDate = splitData[tildeIdx + 1].trim();

        Map<String, String> splitMapData = new HashMap<String, String>();
        splitMapData.put("title", title);
        splitMapData.put("startDate", startDate);
        splitMapData.put("endDate", endDate);

        return splitMapData;
    }

    private String concatTitle(String[] splitData, int idx) {
        String title = "";
        for (int i = 0; i < idx; i++) {
            if(!splitData[i].equals("") && splitData[i] != null)
                title += splitData[i] + " ";
        }
        return title.trim();
    }



    private Elements crawlLectureWeekContentsBox(Document docLecturePage) {
        final String lectureBoxSelector = ".listContent";
        return docLecturePage.select(lectureBoxSelector);
    }





}
