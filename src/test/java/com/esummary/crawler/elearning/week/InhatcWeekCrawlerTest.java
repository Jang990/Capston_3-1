package com.esummary.crawler.elearning.week;

import com.esummary.crawler.elearning.InhatcCrawlerConfig;
import com.esummary.crawler.elearning.week.dto.WeekDTO;
import com.esummary.crawler.elearning.login.InhatcLoginCrawler;
import com.esummary.crawler.elearning.login.LoginCrawler;
import com.esummary.crawler.elearning.week.lecture.InhatcLectureCrawler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class InhatcWeekCrawlerTest {
    WeekCrawler crawler = new InhatcWeekCrawler(new InhatcLectureCrawler());
    LoginCrawler loginCrawler = new InhatcLoginCrawler();

    String courseId = InhatcCrawlerConfig.courseId;
    String id = InhatcCrawlerConfig.id;
    String password = InhatcCrawlerConfig.password;
    Map<String, String> failSession = InhatcCrawlerConfig.failSessionCookie;

    @Test
    @DisplayName("수업정보 정상 크롤링")
    public void crawlLectureTest() throws Exception {
        //given
        Map<String, String> loginSession = loginCrawler.getLoginSession(id, password);

        //when
        List<WeekDTO> weekList = crawler.crawlLecture(courseId, loginSession);

        //then
        assertThat(weekList.size()).isEqualTo(16);
    }
}