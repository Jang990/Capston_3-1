package com.esummary.crawler.announcementcrawler;

import com.esummary.crawler.InhatcCrawlerConfig;
import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawler.logincrawler.InhatcLoginCrawler;
import com.esummary.crawler.logincrawler.LoginCrawler;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

@Slf4j
class InhatcAnnouncementCrawlerTest {

    private final LoginCrawler loginCrawler = new InhatcLoginCrawler();
    private InhatcAnnouncementCrawler crawler = new InhatcAnnouncementCrawler();

    private String id = InhatcCrawlerConfig.id;
    private String password = InhatcCrawlerConfig.password;
    private String courseId = InhatcCrawlerConfig.courseId;

    @BeforeAll
    static void beforeAll() {
        if(InhatcCrawlerConfig.password.equals(InhatcCrawlerConfig.state.EMPTY.toString())) {
            throw new IllegalArgumentException("InhatcLoginCrawlerTest 설정 정보가 모두 필요합니다.");
        }
    }

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