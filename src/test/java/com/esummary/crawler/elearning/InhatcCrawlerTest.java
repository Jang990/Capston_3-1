package com.esummary.crawler.elearning;

import com.esummary.crawler.elearning.announcement.InhatcAnnouncementCrawler;
import com.esummary.crawler.elearning.login.exception.ExpiredELearningSession;
import com.esummary.crawler.elearning.login.InhatcLoginCrawler;
import com.esummary.crawler.elearning.login.LoginCrawler;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class InhatcCrawlerTest {
    private final LoginCrawler loginCrawler = new InhatcLoginCrawler();
    private Crawler crawler = new InhatcCrawler(new InhatcLoginCrawler(), new InhatcAnnouncementCrawler());
    private String id = InhatcCrawlerConfig.id;
    private String password = InhatcCrawlerConfig.password;
    private String failPassword = "1111";

    @BeforeAll
    static void beforeAll() {
        if(InhatcCrawlerConfig.password.equals(InhatcCrawlerConfig.state.EMPTY.toString())) {
            throw new IllegalArgumentException("InhatcLoginCrawlerTest 설정 정보가 모두 필요합니다.");
        }
    }

    @Test
    @DisplayName("로그인 성공")
    public void crawlLoginSession() throws Exception {
        //when
        Map<String, String> loginCookie = crawler.crawlLoginSession(id, password);

        //then
        assertThat(loginCookie.size()).isNotEqualTo(0);
        assertThat(loginCookie.containsKey("JSESSIONID")).isEqualTo(true);
    }

    @Test
    @DisplayName("로그인 실패")
    void crawlLoginSessionFail() {
        assertThrows(ExpiredELearningSession.class,
                () -> crawler.crawlLoginSession(id, failPassword));
    }

    @Test
    void crawlOwnCourse() {
    }

    @Test
    void crawlLectureByWeek() {
    }

    @Test
    void crawlAnnouncement() {
    }

    @Test
    void crawlAssignment() throws IOException {
        Map<String, String> cookies = Jsoup.connect("https://cyber.inhatc.ac.kr/MMain.do?cmd=viewIndexPage")
                .method(Connection.Method.GET)
                .execute().cookies();
        System.out.println("cookies = " + cookies);

        Connection conn = Jsoup.connect("https://cyber.inhatc.ac.kr/MUser.do")
                .data("cmd", "loginUser")
                .data("userDTO.userId", id)
                .data("userDTO.password", password)
                .data("userDTO.localeKey", "ko")
                .cookies(cookies);

        Document post = conn.post();
        System.out.println("conn = " + post);
    }
}