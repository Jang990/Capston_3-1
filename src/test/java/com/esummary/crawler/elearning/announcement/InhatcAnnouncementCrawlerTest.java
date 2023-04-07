package com.esummary.crawler.elearning.announcement;

import com.esummary.crawler.connection.PageConnector;
import com.esummary.crawler.elearning.InhatcCrawlerConfig;
import com.esummary.crawler.elearning.announcement.dto.AnnouncementDTO;
import com.esummary.crawler.elearning.dto.ContentDetail;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class InhatcAnnouncementCrawlerTest {

    @InjectMocks
    private InhatcAnnouncementCrawler crawler;
    @Mock
    private PageConnector connector;

    private String testCourseId = "";
    private Map<String, String> testCookies = new HashMap<>();

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
        AnnouncementDTO announcement1 = createAnnouncement1();
        AnnouncementDTO announcement2 = createAnnouncement2();

        when(connector.getContent(any())).thenReturn(Jsoup.parse(createDocument(announcement1, announcement2)));

        //when
        List<AnnouncementDTO> announcementList = crawler.crawlAnnouncement(testCourseId, testCookies);

        //then
        assertThat(announcementList.size()).isEqualTo(2);


        checkDTO(announcementList.get(0), announcement1);
        checkDTO(announcementList.get(1), announcement2);
    }

    private void checkDTO(AnnouncementDTO findDTO, AnnouncementDTO givenDTO) {
        assertThat(findDTO.getContentDetail().getContent()).isEqualTo(givenDTO.getContentDetail().getContent());
        assertThat(findDTO.getContentDetail().getId()).isEqualTo(givenDTO.getContentDetail().getId());
        assertThat(findDTO.getContentDetail().getTitle()).isEqualTo(givenDTO.getContentDetail().getTitle());

        assertThat(findDTO.getAnnouncementDate()).isEqualTo(givenDTO.getAnnouncementDate());
    }

    private AnnouncementDTO createAnnouncement1() {
        ContentDetail contentDetail = new ContentDetail("BOAD_22103114402885342707", "캡스톤디자인 최종 발표 및 최종보고서 제출", "11월 이후 캡스톤 디자인 수업이 아래 처럼 진행되니 참고하기 바랍니다.");
        LocalDateTime announcementDate = LocalDateTime.of(22, 10, 31, 0,0);
        return new AnnouncementDTO(contentDetail, announcementDate);
    }
    private AnnouncementDTO createAnnouncement2() {
        ContentDetail contentDetail = new ContentDetail("BOAD_2208291537067f8300e6", "심화 캡스톤디자인 교과목을 시작하며...", "안녕하세요. 마지막 방학...잘 보냈는지요...");
        LocalDateTime announcementDate = LocalDateTime.of(22, 8, 29, 0,0);
        return new AnnouncementDTO(contentDetail, announcementDate);
    }

    @Test
    @DisplayName("잘못된 SessionID가 들어온 경우")
    public void announcementCrawlerTestFail() throws Exception {
        //given
        when(connector.getContent(any())).thenReturn(Jsoup.parse(createFailDocument()));
        //when
        List<AnnouncementDTO> announcementList = crawler.crawlAnnouncement(testCourseId, testCookies);
        //then
        assertThat(announcementList.size()).isEqualTo(0);
    }

    private String createDocument(AnnouncementDTO... announcementDTOS) {
        String doc = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xml:lang=\"ko-kr\" lang=\"ko-kr\" xmlns=\"http://www.w3.org/1999/xhtml\"> \n" +
                " <head> \n" +
                " </head> \n" +
                " <body> \n" +
                "<div id=\"listBox\">";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (AnnouncementDTO announcementDTO : announcementDTOS) {
            String fomat = "        <div class=\"listContent\"> \n" +
                    "         <dl class=\"element\"> \n" +
                    "          <dt> \n" +
                    "           <h4 class=\"f14 fontB\"> <i class=\"icon-info-black mt-5 mr5\"></i> <a class=\"boardTitleNcontent TITLE_ORIGIN\" href=\"#0\" onclick=\"javascript:moveBoardView('%s', '2');\"> <font color=\"\"></font> %s </a> </h4> \n" +
                    "           <ul class=\"btnBox\"> <!-- \n" +
                    "\t\t\t\t\t\t\t\t\t\t<li><a class=\"btn small\" href=\"#\"><i class=\"icon-print-small-color\"></i>인 쇄</a></li>\n" +
                    "\t\t\t\t\t\t\t\t\t\t --> \n" +
                    "            <li> <button type=\"submit\" class=\"btn small\" onclick=\"javascript:moveBoardView('%s', '2');return false;\"> <i class=\"icon-report-small-color\"></i> 상 세 </button> </li> \n" +
                    "           </ul> \n" +
                    "          </dt> \n" +
                    "          <dd class=\"info\"> \n" +
                    "           <ul class=\"fl ml10\"> \n" +
                    "            <input type=\"hidden\" name=\"fileListSize\" value=\"2\"> \n" +
                    "            <li class=\"fl mr10\"> </li> \n" +
                    "            <li class=\"fl mr10\"> </li> \n" +
                    "           </ul> \n" +
                    "           <ul class=\"fr mr10\"> \n" +
                    "            <li><b>작성자 :</b> <span class=\"boardTitleNcontent REG_NAME\">황수철</span> </li> \n" +
                    "            <li><b>작성일 :</b> %s</li> \n" +
                    "            <li class=\"m0\"><b>조회수 :</b> 1</li> \n" +
                    "           </ul> \n" +
                    "          </dd> \n" +
                    "          <dd> \n" +
                    "           <div class=\"cont boardTitleNcontent BOARD_CONTENTS_ORIGIN\">\n" +
                    "            <p>%s</p>\n" +
                    "           </div> \n" +
                    "          </dd> \n" +
                    "         </dl> <!-- 답변 영역 부분 --> <!-- //답변 영역 부분 --> \n" +
                    "        </div> <!--내용끝 --> \n";
            ContentDetail contentDetail = announcementDTO.getContentDetail();
            doc += String.format(fomat,
                    contentDetail.getId(),
                    contentDetail.getTitle(),
                    contentDetail.getId(),
                    formatter.format(announcementDTO.getAnnouncementDate()),
                    contentDetail.getContent());
        }

        doc += "   </div>" +
                " </body>\n" +
                "</html>\n";

        return doc;
    }

    String createFailDocument() {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html>\n" +
                " <head>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <div id=\"listBox\"> \n" +
                "   <div class=\"error_none\"> \n" +
                "    <ul> \n" +
                "     <li>이용시간이 경과되었습니다.</li> \n" +
                "     <li>장시간 미사용으로 로그아웃되었습니다.</li> \n" +
                "     <li>원활한 이용을 위해 다시 로그인 해 주시기 바랍니다.</li> \n" +
                "     <li style=\"background:none;\"> <a href=\"/Main.do?cmd=viewHome&amp;userDTO.localeKey=ko\" class=\"btn w100\"> 확 인</a> </li> \n" +
                "    </ul> \n" +
                "    <p></p> \n" +
                "   </div> \n" +
                "  </div> \n" +
                " </body>\n" +
                "</html>\n";
    }
}