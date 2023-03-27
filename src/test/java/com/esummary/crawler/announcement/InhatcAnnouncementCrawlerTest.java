package com.esummary.crawler.announcement;

import com.esummary.crawler.InhatcCrawlerConfig;
import com.esummary.crawler.announcement.dto.AnnouncementDTO;
import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import com.esummary.crawler.logincrawler.LoginCrawler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
class InhatcAnnouncementCrawlerTest {

    private final LoginCrawler loginCrawler = new InhatcLoginCrawler();
    private InhatcAnnouncementCrawler crawler = new InhatcAnnouncementCrawler();

    private String id = InhatcCrawlerConfig.id;
    private String password = InhatcCrawlerConfig.password;
    private String courseId = InhatcCrawlerConfig.courseId;
    private Map<String, String> failSessionCookie = InhatcCrawlerConfig.failSessionCookie;

    @BeforeAll
    static void beforeAll() {
        if(InhatcCrawlerConfig.password.equals(InhatcCrawlerConfig.state.EMPTY.toString())) {
            throw new IllegalArgumentException("InhatcLoginCrawlerTest 설정 정보가 모두 필요합니다.");
        }
    }

    @Test
    @DisplayName("공지 크롤링 정상 작동")
    void announcementCrawlerTest() throws Exception {
        //given
        Map<String, String> loginSessionCookie = loginCrawler.getLoginSession(id, password);

        //when
        List<AnnouncementDTO> announcementList = crawler.crawlAnnouncement(courseId, loginSessionCookie);

        //then
        assertThat(announcementList.size()).isEqualTo(3);
        for (AnnouncementDTO announcementDTO : announcementList) {
            System.out.println("announcementDTO = " + announcementDTO);
        }
    }

    @Test
    @DisplayName("잘못된 SessionID가 들어온 경우")
    public void announcementCrawlerTestFail() throws Exception {
        //when, then
        List<AnnouncementDTO> announcementList = crawler.crawlAnnouncement(courseId, failSessionCookie);

        assertThat(announcementList.size()).isEqualTo(0);
    }
}