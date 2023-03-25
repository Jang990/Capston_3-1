package com.esummary.crawler.assignment;

import com.esummary.crawler.dto.AssignmentDTO;
import com.esummary.crawler.logincrawler.LoginCrawler;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class InhatcAssignmentCrawler implements AssignmentCrawler {

//    private final String STUDY_HOME_URL = "https://cyber.inhatc.ac.kr/Course.do?cmd=viewStudyHome&courseDTO.courseId=";
    private final String ASSIGNMENT_HOME_URL = "https://cyber.inhatc.ac.kr/Report.do?cmd=viewReportInfoPageList&boardInfoDTO.boardInfoGubun=report&courseDTO.courseId=";

    private final LoginCrawler inhatcLoginCrawler;

    @Override
    public List<AssignmentDTO> crawlAssignment(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        inhatcLoginCrawler.validateExpiredSession(loginSessionCookie);

        List<AssignmentDTO> assignmentList = new ArrayList<>();
        Elements assignments = crawlAssignmentBox(courseId, loginSessionCookie);

        for (Element element : assignments) {
//            System.out.println("element = " + element);
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


        return assignmentPage.select(assignmentBoxSelector);
    }

    private AssignmentDTO crawlAssignmentDetail(Element element) {
        String[] idAndStatus = crawlIdAndStatus(element);
        /*
         * 여기 조건부는 상황에 따라 달라질 수 있다.
         * 과제 시작 기간이 아직 안되서 이러닝에는 있고 제출버튼이 없을 경우...
         * 제출버튼이 없어도 과제 내용을 보고 싶은 경우가 있다. 그럴때는 null도 받아서 보여줘야 할 것이다.
         */
        if(idAndStatus == null || idAndStatus[0].equals(""))
            return null;
        String id = idAndStatus[0];
        String status =
                (idAndStatus.length == 2) ? idAndStatus[1] : "Y";
        String title = crawlTitle(element);
        String deadline = crawlDeadline(element);
        String submissionInfo = crawlSubmissionInfo(element);
        String description = crawlDescription(element);
//        Map<String, Date> rangeDate = splitDataInRangeString(deadline);
        Map<String, Integer> submissionData = splitSubmissionData(submissionInfo);

        AssignmentDTO assignment = new AssignmentDTO(
        /*        id, title, description,
                rangeDate.get("startDate"), rangeDate.get("endDate"),
                submissionData.get("submissionNum"), submissionData.get("notSubmittedNum"), submissionData.get("totalNum"),
                status, subjectId*/
        );
        return assignment;
    }

    private String[] crawlIdAndStatus(Element element) {
        final String idSelector = ".btnBox > li:nth-child(2) > a";
        String idAndStatus_JS = element.select(idSelector).attr("onclick");
        return null;
//        return SubjectCrawlingService_Inhatc.extractDataFromJsCode(idAndStatus_JS);
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

    private Map<String, Integer> splitSubmissionData(String submissionInfo) {
        String[] submissionString = submissionInfo.split("/");
        int[] submissionInteger = new int[submissionString.length];
        for (int i = 0; i < submissionString.length; i++) {
            submissionString[i] = submissionString[i].replace("명", "");
            submissionInteger[i] = Integer.parseInt(submissionString[i].trim());
        }

        Map<String, Integer> submissionData = new HashMap<String, Integer>();
        submissionData.put("submissionNum", submissionInteger[0]);
        submissionData.put("notSubmittedNum", submissionInteger[1]);
        submissionData.put("totalNum", submissionInteger[3]);

        return submissionData;
    }

    private Map<String, Date> splitDataInRangeString(String deadLineString) {
		/*
		2022-02-27 ~ 2022-06-17
		이 데이터를
		StartDate / EndDate로 나눈다.
		2022-02-27 / 2022-06-17
		*/

        /*
        String[] splitData = deadLineString.split(" ");
        int tildeIdx = SubjectCrawlingService_Inhatc.findTildeIdx(splitData);

        if(tildeIdx == 0) return null;

        String startDateString = splitData[tildeIdx - 2].trim() + " " + splitData[tildeIdx - 1].trim();
        String endDateString = splitData[tildeIdx + 1].trim() + " " + splitData[tildeIdx + 2].trim();

        Map<String, Date> submitRange = new HashMap<String, Date>();

        Date startDate = SubjectCrawlingService_Inhatc.parseDate(startDateString);
        Date endDate = SubjectCrawlingService_Inhatc.parseDate(endDateString);
        submitRange.put("startDate", startDate);
        submitRange.put("endDate", endDate);

        return submitRange;
        */
        return null;
    }
}
