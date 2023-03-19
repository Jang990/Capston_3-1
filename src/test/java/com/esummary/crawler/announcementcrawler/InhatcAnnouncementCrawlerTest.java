package com.esummary.crawler.announcementcrawler;

import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import com.esummary.crawler.logincrawler.LoginCrawler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InhatcAnnouncementCrawlerTest {
    private final LoginCrawler loginCrawler = new InhatcLoginCrawler();
    private InhatcAnnouncementCrawler crawler = new InhatcAnnouncementCrawler();

    private String id = "201845096";
    private String password = ".";
    private String courseId = "202224043DMP636";

    @Test
    public void announcementCrawlerTest() throws Exception {
        //given
        Map<String, String> loginSessionCookie =
                loginCrawler.getLoginSession(id, password).orElseThrow(() -> new Exception());

        //when
        List<AnnouncementDTO> announcementList = crawler.crawlAnnouncement(courseId, loginSessionCookie);

        //then
        Assertions.assertThat(announcementList.size()).isEqualTo(3);
        for (AnnouncementDTO announcementDTO : announcementList) {
            System.out.println("announcementDTO = " + announcementDTO);
        }
    }
}