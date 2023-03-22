package com.esummary.crawler;

import com.esummary.crawler.announcementcrawler.InhatcAnnouncementCrawler;
import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import com.esummary.crawler.logincrawler.LoginCrawler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

class InhatcCrawlerTest {
    private Crawler crawler = new InhatcCrawler(new InhatcLoginCrawler(), new InhatcAnnouncementCrawler());

    /*
    @Autowired
    private Crawler crawler;
    */

    private final LoginCrawler loginCrawler = new InhatcLoginCrawler();
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
    @DisplayName("로그인 실패")
    void crawlLoginSession() {
        Optional<Map<String, String>> loginCookie = crawler.crawlLoginSession(id, failPassword);
        Assertions.assertThat(loginCookie.isEmpty()).isEqualTo(true);
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
    void crawlAssignment() {
    }
}