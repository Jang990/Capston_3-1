package com.esummary.crawler.week.lecture;

import com.esummary.crawler.dto.ContentCompletionStatus;
import com.esummary.crawler.week.dto.LectureDTO;
import com.esummary.crawler.week.dto.LectureTime;
import com.esummary.crawler.week.dto.LectureType;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class InhatcLectureCrawler {
    public List<LectureDTO> getLectureList(Elements lectureElements) {
        List<LectureDTO> lectures = new ArrayList<>();

        for (Element element : lectureElements) {
            LectureDTO lecture = getLecture(element);
            if (lecture == null) {
                continue;
            }
            lectures.add(lecture);
        }

        return lectures;
    }

    private LectureDTO getLecture(Element element) {
        int idx = getIdx(element);
        LectureType type = getType(element);
        String title = crawlTitle(element);

        LectureTime lectureTime = getTime(element);
        ContentCompletionStatus status = getStatus(element);

        return new LectureDTO(idx, type, title, lectureTime, status);
    }

    private LectureTime getTime(Element element) {
        String totalTime = crawlFulltime(element);
        String watchedTime = crawlLearningTime(element);

        LocalTime totalLocalTime = convertStringToLocalTime(totalTime);
        LocalTime watchedLocalTime = convertStringToLocalTime(watchedTime);

        return new LectureTime(totalLocalTime, watchedLocalTime);
    }

    private LocalTime convertStringToLocalTime(String timeString) {
        // "0 초", "51분 30초", "52 분"
        int hours = 0, minutes = 0, seconds = 0;

        String[] timeArray = timeString.replace("분","")
                .replace("초","")
                .trim().split(" ");

        if(timeString.contains("분")) {
            minutes = Integer.parseInt(timeArray[0])%60;
            hours = Integer.parseInt(timeArray[0])/60;
        }
        if(timeString.contains("초")) {
            seconds = Integer.parseInt(timeArray[timeArray.length-1]);
        }

        LocalTime time = LocalTime.of(hours, minutes, seconds);

        return time;
    }

    private ContentCompletionStatus getStatus(Element element) {
        String status = crawlStatus(element);

        if(status.equals("full")) // full O
            return ContentCompletionStatus.Completed;

        if(status.equals("half")) // half 세모
            return ContentCompletionStatus.Incomplete;

        return ContentCompletionStatus.Insufficient; // empty X
    }

    private String crawlLearningTime(Element element) {
        final String learningTimeSelector = "tr > td:nth-child(6) > span";
        return element.select(learningTimeSelector).text().replace("TOTAL : ", "");
    }

    private String crawlFulltime(Element element) {
        final String fullTimeSelector = "tr > td:nth-child(5)";
        return element.select(fullTimeSelector).text();
    }

    private String crawlStatus(Element element) {
        final String statusSelector = "tr > td:nth-child(4) > img";
        String status = element.select(statusSelector).attr("src")
                .replace("/lmsdata/img_common/icon/set1/icon_", "")
                .replace("_print.gif", "").trim();
        return status;
    }

    private String crawlTitle(Element element) {
        final String titleSelector = "tr > td:nth-child(3)";
        return element.select(titleSelector).text();
    }

    private LectureType getType(Element element) {
        final String typeSelector = "tr > td:nth-child(2)"; // 굳이 있어야 하나?
        String typeText = element.select(typeSelector).text().trim();

        if(typeText.equals("온라인"))
            return LectureType.VIDEO;
        if(typeText.equals("화상강의"))
            return LectureType.ONLINE;

        return LectureType.SLIDE;
    }

    private int getIdx(Element element) {
        final String idxSelector = "tr > td:nth-child(1)";
        String idxText = element.select(idxSelector).text();
        return Integer.parseInt(idxText);
    }
}
