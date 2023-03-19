package com.esummary.crawler;

import com.esummary.crawler.announcementcrawler.InhatcAnnouncementCrawler;
import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import org.assertj.core.api.Assertions;
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

    @Test
    @DisplayName("로그인 실패")
    void crawlLoginSession() {
        Optional<Map<String, String>> loginCookie = crawler.crawlLoginSession("201845096", "1232132");
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