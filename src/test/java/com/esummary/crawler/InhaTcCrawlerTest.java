package com.esummary.crawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InhaTcCrawlerTest {
    private Crawler crawler = new InhaTcCrawler();

    @Test
    @DisplayName("로그인 실패")
    void crawlLoginSession() {
        Map<String, String> loginCookie = crawler.crawlLoginSession("201845096", "ABCDEFG");
        Assertions.assertThat(loginCookie).isEqualTo(null);
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