package com.esummary.crawler.elearning.assignment;

import com.esummary.crawler.elearning.assignment.dto.AssignmentDTO;
import com.esummary.crawler.elearning.assignment.dto.AssignmentSubmissionStatus;
import com.esummary.crawler.elearning.dto.ContentCompletionStatus;
import com.esummary.crawler.elearning.dto.ContentDetail;
import com.esummary.crawler.elearning.dto.ContentPeriod;
import com.esummary.crawler.elearning.util.InhatcUtil;
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
public class InhatcAssignmentCrawler implements AssignmentCrawler {
    private final String ASSIGNMENT_HOME_URL = "https://cyber.inhatc.ac.kr/Report.do?cmd=viewReportInfoPageList&boardInfoDTO.boardInfoGubun=report&courseDTO.courseId=";

    @Override
    public List<AssignmentDTO> crawlAssignment(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        List<AssignmentDTO> assignmentList = new ArrayList<>();
        Elements assignments = crawlAssignmentBox(courseId, loginSessionCookie);

        for (Element element : assignments) {
            if(!InhatcUtil.isContent(element))
                continue;

            AssignmentDTO assignment = crawlAssignmentDetail(element);
            assignmentList.add(assignment);
        }

        return assignmentList;
    }

    private Elements crawlAssignmentBox(String courseId, Map<String, String> loginCookies) throws IOException {
        //StudyHome에서 과제 내용이 적혀있는 섹션에 css Selector
        final String assignmentBoxSelector = "#listBox > div:not(.paginator_pages):not(.paginator)";
        Document assignmentPage = null;
        assignmentPage = Jsoup.connect(ASSIGNMENT_HOME_URL +courseId)
                .data("cmd", "viewIndexPage")
                .cookies(loginCookies).get();

        Elements select = assignmentPage.select(assignmentBoxSelector);
        return select;
    }

    private AssignmentDTO crawlAssignmentDetail(Element element) {
        ContentDetail contentDetail = getContentDetail(element);
        ContentCompletionStatus status = getStatus(element);
        ContentPeriod contentPeriod = getAssignmentPeriod(element);
        AssignmentSubmissionStatus submissionStatus = getAssignmentSubmissionStatus(element);

        AssignmentDTO assignment = new AssignmentDTO(
                contentDetail,
                contentPeriod,
                submissionStatus,
                status
        );

        return assignment;
    }

    private ContentPeriod getAssignmentPeriod(Element element) {
        String deadline = crawlDeadline(element);

//        System.out.println("deadline = " + deadline);

        String[] splitData = deadline.split(" ");
        int tildeIdx = InhatcUtil.findTildeIdx(splitData);

        if(tildeIdx == 0) return null;

        String startDateString = splitData[tildeIdx - 2].trim() + " " + splitData[tildeIdx - 1].trim();
        String endDateString = splitData[tildeIdx + 1].trim() + " " + splitData[tildeIdx + 2].trim();

        LocalDateTime startDate = InhatcUtil.parseDate(startDateString);
        LocalDateTime endDate = InhatcUtil.parseDate(endDateString);

        return new ContentPeriod(startDate, endDate);
    }

    private ContentDetail getContentDetail(Element element) {
        String assignmentIdAndStatus = crawlAssignmentId(element);
        /*
         * 여기 조건부는 상황에 따라 달라질 수 있다.
         * 과제 시작 기간이 아직 안되서 이러닝에는 있고 제출버튼이 없을 경우...
         * 제출버튼이 없어도 과제 내용을 보고 싶은 경우가 있다. 그럴때는 null도 받아서 보여줘야 할 것이다.
         */
        if(assignmentIdAndStatus == null || assignmentIdAndStatus.equals(""))
            return null;

        String id = assignmentIdAndStatus;
        String title = crawlTitle(element);
        String content = crawlDescription(element);

        return new ContentDetail(id,title, content);
    }

    private AssignmentSubmissionStatus getAssignmentSubmissionStatus(Element element) {
        String submissionInfo = crawlSubmissionInfo(element);

        String[] submissionString = submissionInfo.split("/");
        int[] submissionInteger = new int[submissionString.length];
        for (int i = 0; i < submissionString.length; i++) {
            submissionString[i] = submissionString[i].replace("명", "");
            submissionInteger[i] = Integer.parseInt(submissionString[i].trim());
        }

        return new AssignmentSubmissionStatus(submissionInteger[0], submissionInteger[1],submissionInteger[3]);
    }

    private ContentCompletionStatus getStatus(Element element) {
        String status = crawlStatus(element);
        if (status.equals("제출정보보기")) {
            return ContentCompletionStatus.Completed;
        }

        return ContentCompletionStatus.Insufficient;
    }

    private String crawlStatus(Element element) {
        final String statusSelector =  "div > dl > dt > ul > li:nth-child(2) > a";
        return element.select(statusSelector).text();
    }

    private String crawlAssignmentId(Element element) {
        final String idSelector = ".btnBox > li:nth-child(2) > a";
        String idAndStatus_JS = element.select(idSelector).attr("onclick");
        String[] IdAndStatus = InhatcUtil.extractDataFromJsCode(idAndStatus_JS);
        return IdAndStatus[0];
    }

    private String crawlDescription(Element element) {
        final String descriptionSelector = "div > dl > dd:nth-child(4) > div";
        return element.select(descriptionSelector).text().trim();
    }

    private String crawlSubmissionInfo(Element element) {
        final String submissionInfoSelector = "div > dl > dd:nth-child(3) > table > tbody > tr > td.last";
        return element.select(submissionInfoSelector).text().trim();
    }

    private String crawlDeadline(Element element) {
        final String deadlineSelector = "div > dl > dd:nth-child(3) > table > tbody > tr > td:first-child";
        return element.select(deadlineSelector).text().trim();
    }

    private String crawlTitle(Element element) {
        final String titleSelector = "div > dl > dt > h4";
        return element.select(titleSelector).text().trim();
    }
}
