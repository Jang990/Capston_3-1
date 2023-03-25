package com.esummary.crawler.announcement;

import com.esummary.crawler.InhatcCrawlerConfig;
import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import com.esummary.crawler.logincrawler.LoginCrawler;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
        Assertions.assertThat(announcementList.size()).isEqualTo(3);
        for (AnnouncementDTO announcementDTO : announcementList) {
            System.out.println("announcementDTO = " + announcementDTO);
        }
    }

    @Test
    @DisplayName("잘못된 SessionID가 들어온 경우")
    public void announcementCrawlerTestFail() throws Exception {
        //when
        List<AnnouncementDTO> announcementDTOList = crawler.crawlAnnouncement(courseId, failSessionCookie);

        //then
        Assertions.assertThat(announcementDTOList.size()).isEqualTo(0);
    }
}