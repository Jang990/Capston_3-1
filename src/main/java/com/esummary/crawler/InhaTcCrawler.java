package com.esummary.crawler;

import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawler.dto.AssignmentDTO;
import com.esummary.crawler.dto.CourseDTO;
import com.esummary.crawler.dto.WeekDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class InhaTcCrawler implements Crawler {
//    public final String MAIN_URL = "https://cyber.inhatc.ac.kr";
    private final String INIT_COOKIE_URL = "https://cyber.inhatc.ac.kr/MMain.do?cmd=viewIndexPage";
    private final String LOGIN_URL = "https://cyber.inhatc.ac.kr/MUser.do";
    @Override
    public Map<String, String> crawlLoginSession(String id, String password) {
        Map<String, String> initialCookies = null;

        // 초기 쿠키 가져오기
        try {
            Connection.Response initial = Jsoup.connect(INIT_COOKIE_URL)
                    .method(Connection.Method.GET)
                    .execute();
            initialCookies = initial.cookies();
        } catch (IOException e) {
            // 이러닝 페이지 다운
            e.printStackTrace();
            return null;
        }

        // 로그인 시도
        Connection con = Jsoup.connect(LOGIN_URL)
                .data("cmd", "loginUser")
                .data("userDTO.userId", id)
                .data("userDTO.password", password)
                .data("userDTO.localeKey", "ko")
                .cookies(initialCookies);

        try {
            Connection.Response resp = con.execute();
            if(resp.statusCode() != 200) {
                // 사이트 다운
                return null;
            }

            con.post();
        } catch (IOException e) {
            // 이러닝 페이지 다운
            e.printStackTrace();
            return null;
        }


        // 로그인에 성공했는지 확인
        // 로그인 페이지 연결
        Document loginPage = null;
        try {
            Connection connectionLoginPage = Jsoup.connect(INIT_COOKIE_URL)
                    .data("cmd", "viewIndexPage")
                    .cookies(initialCookies);
            Connection.Response resp = connectionLoginPage.execute();

            if(resp.statusCode() == 200)
                loginPage = connectionLoginPage.post();
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // 로그인을 해서 이름이 나오는지 체크
        Element str = loginPage.getElementsByClass("login_info").select("ul li").last(); //정보를 찾을 수 없음. 즉 로그인이 되지 않은 쿠키라는 것(또는 만료된 로그인 쿠키라는 것)
        if(str == null)
            return null;
        String[] nameAndWStudentNumber = str.text().split(" ");
        String studentName = nameAndWStudentNumber[0].trim();
        if(studentName == null) {
            return null;
        }


        return initialCookies; // 로그인 성공.
    }

    @Override
    public List<CourseDTO> crawlOwnCourse(Map<String, String> loginSession) {
        return null;
    }

    @Override
    public List<WeekDTO> crawlLectureByWeek(Map<String, String> loginSession) {
        return null;
    }

    @Override
    public List<AnnouncementDTO> crawlAnnouncement(Map<String, String> loginSession) {
        return null;
    }

    @Override
    public List<AssignmentDTO> crawlAssignment(Map<String, String> loginSession) {
        return null;
    }
}
