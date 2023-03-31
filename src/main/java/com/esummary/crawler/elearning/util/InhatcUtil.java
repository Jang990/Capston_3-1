package com.esummary.crawler.elearning.util;

import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InhatcUtil {
    public static boolean isContent(Element element) {
        String elementClassName = element.getElementsByTag("div").get(0).className();
        return elementClassName.contains("listContent");
    }

    public static LocalDateTime parseDate(String dateString) {
        DateTimeFormatter dtf;
        if(dateString.contains(":")) {
            dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); //과제
            return LocalDateTime.parse(dateString, dtf);
        }

        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //주차
        return LocalDate.parse(dateString, dtf).atStartOfDay();
    }

    public static String[] extractDataFromJsCode(String attr) {
        String unnecessaryString= "javascript:" + foundUnnecessaryString(attr) + "(";

        String[] taskData = attr.replace(unnecessaryString, "")
                .replace(");", "")
                .replace("'", "")
                .split(",");

        for(int i = 0; i < taskData.length; i++)
            taskData[i] = taskData[i].trim();

        return taskData;
    }

    /**
     * "aaa" / "2022-02-27" / "~" / "2022-06-17" <br>
     * 위와 같은 데이터에서 ~ 의 위치를 알아냄
     * @param splitData
     * @return '~'의 인덱스번호. '~'가 없을 시 0을 반환.
     */
    public static int findTildeIdx(String[] splitData) {
        int tildeIdx = 0;

        for (int i = 0; i < splitData.length; i++) {
            if(splitData[i].equals("~")) {
                tildeIdx = i;
                break;
            }
        }

        return tildeIdx;
    }

    private static String foundUnnecessaryString(String attr) {
        if(attr.contains("ViewReportContent"))
            return "ViewReportContent"; //과제일때
        else if(attr.contains("ViewBoardContent"))
            return "ViewBoardContent"; //공지사항일때
        else if(attr.contains("submitReportView"))
            return "submitReportView"; //??
        else if(attr.contains("submitReport"))
            return "submitReport"; //과제가 현재 진행중(제출하기 버튼일 때)
        else if(attr.contains("viewStudyContents")) //학습하기일때
            return "viewStudyContents";
//		else if(attr.contains("viewStudyBoard"))
//			return "viewStudyBoard"; //학습활동글쓰기일때 모르겠다
        else if(attr.contains("moveBoardView"))
            return "moveBoardView";
        else
            return null;
    }
}
