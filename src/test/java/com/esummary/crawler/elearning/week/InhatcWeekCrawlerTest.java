package com.esummary.crawler.elearning.week;

import com.esummary.crawler.connection.PageConnector;
import com.esummary.crawler.elearning.dto.ContentDetail;
import com.esummary.crawler.elearning.dto.ContentPeriod;
import com.esummary.crawler.elearning.week.dto.WeekDTO;
import com.esummary.crawler.elearning.week.lecture.InhatcLectureCrawler;
import com.esummary.crawler.elearning.week.lecture.dto.LectureDTO;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InhatcWeekCrawlerTest {

    @InjectMocks
    InhatcWeekCrawler crawler;
    @Mock
    InhatcLectureCrawler lectureCrawler;
    @Mock
    PageConnector connector;

    private String testCourseId = "testCourse";
    private Map<String, String> testCookies = new HashMap<>();

    @Test
    @DisplayName("수업정보 정상 크롤링")
    public void crawlLectureTest() throws Exception {
        //given
        ContentDetail contentDetail = createContentDetail();
        ContentPeriod contentPeriod = createPeriod();
        List<LectureDTO> lectureList = createLecture();
        WeekDTO week = new WeekDTO(contentDetail, contentPeriod, lectureList);
        String docString = creatDocumentString(week);

        when(lectureCrawler.getLectureList(any())).thenReturn(lectureList);
        when(connector.getContent(any())).thenReturn(Jsoup.parse(docString));

        //when
        List<WeekDTO> weekList = crawler.crawlLecture(testCourseId, testCookies);
//        List<WeekDTO> weekList = crawler.crawlLecture(courseId, loginCookie);
        for (WeekDTO weekDTO : weekList) {
            System.out.println("weekDTO = " + weekDTO);
        }

        //then
        assertThat(weekList.size()).isEqualTo(2);
    }

    private List<LectureDTO> createLecture() {
        ArrayList<LectureDTO> lectureList = new ArrayList<>();
//        weekDTO = WeekDTO(lectureList=[LectureDTO(idx=1, type=VIDEO, title=실험실습실 안전교육, time=LectureTime(totalTime=00:00, watchedTime=00:00), status=Insufficient), LectureDTO(idx=2, type=VIDEO, title=교과목 오리엔테이션, time=LectureTime(totalTime=00:00, watchedTime=00:00), status=Insufficient), LectureDTO(idx=3, type=VIDEO, title=개발방법(1), time=LectureTime(totalTime=00:55, watchedTime=00:00), status=Insufficient)])

        return lectureList;
    }

    private ContentPeriod createPeriod() {
        LocalDateTime start = LocalDateTime.of(22, 8, 29, 0, 0);
        LocalDateTime end = LocalDateTime.of(22, 12, 16, 0, 0);
        return new ContentPeriod(start, end);
    }

    private ContentDetail createContentDetail() {
        return new ContentDetail("LESN_2208291538367f8300f2", "1주차", null);
    }

    private String creatDocumentString(WeekDTO week) {
        return "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xml:lang=\"ko-kr\" lang=\"ko-kr\" xmlns=\"http://www.w3.org/1999/xhtml\"> \n" +
                " <head> \n" +
                " </head> \n" +
                " <body> \n" +
                "      <div id=\"listBox\"> \n" +
                "       <div class=\"listContent\"> \n" +
                "        <dl class=\"element\"> \n" +
                "         <dt> \n" +
                "          <h4 class=\"f14 fontB\"> <i class=\"icon-calendar-color\"></i> 15주차 &nbsp;&nbsp; 2022-08-29 ~ 2022-12-16 <span class=\"btn mini fontHelvetica fwb\" style=\"margin-left:10px;\" href=\"#\" onclick=\"contentsInfoPop('LESN_2208291538367f830100');return false;\">학습목표보기</span> </h4> \n" +
                "         </dt> <!-- 학습요소 START --> \n" +
                "         <dd> \n" +
                "          <div class=\"cont\"> \n" +
                "           <table class=\"boardListBasic\" summary=\"교안관리 주차 리스트입니다.\"> \n" +
                "            <caption>\n" +
                "             교안관리 리스트\n" +
                "            </caption> \n" +
                "            <thead> \n" +
                "             <tr> \n" +
                "              <th class=\"num\">번 호</th> \n" +
                "              <th>구 분</th> \n" +
                "              <th>차시명</th> \n" +
                "              <th>학습현황</th> \n" +
                "              <th> 학습시간 / 수업일자 </th> <!-- \t<th>통과 점수</th> --> \n" +
                "              <th>출결 정보(기간 내)</th> \n" +
                "              <th>출결 정보(기간 후)</th> \n" +
                "              <th>학습 하기</th> \n" +
                "             </tr> \n" +
                "            </thead> \n" +
                "            <tbody>\n" +
                "             <tr> \n" +
                "              <td>1</td> \n" +
                "              <td>온라인</td> \n" +
                "              <td>최종 발표</td> \n" +
                "              <td> <img src=\"/lmsdata/img_common/icon/set1/icon_empty_print.gif\"> </td> \n" +
                "              <td> 0분 </td> \n" +
                "              <td class=\"textLeft\"> <span class=\"f14 fwb fcBlack pb10\" style=\"display:block\">TOTAL : 0 초</span> \n" +
                "               <ul> \n" +
                "                <li><span style=\"display:inline-block; width:60px;\">웹</span> <span> 0 초 </span> </li> \n" +
                "                <li><span style=\"display:inline-block; width:60px;\">모바일</span> <span> 0 초 </span> </li> \n" +
                "               </ul> </td> \n" +
                "              <td class=\"textLeft\"> <span class=\"f14 fwb fcBlack pb10\" style=\"display:block\">TOTAL : 0 초</span> \n" +
                "               <ul> \n" +
                "                <li><span style=\"display:inline-block; width:60px;\">웹</span> <span> 0 초 </span> </li> \n" +
                "                <li><span style=\"display:inline-block; width:60px;\">모바일</span> <span> 0 초 </span> </li> \n" +
                "               </ul> </td> \n" +
                "              <td> <p class=\"btn-group\"> </p> </td> \n" +
                "             </tr> \n" +
                "            </tbody>\n" +
                "           </table> \n" +
                "          </div> \n" +
                "         </dd> <!-- 학습요소 END --> \n" +
                "        </dl> \n" +
                "       </div> \n" +
                "      </div> \n" +
                "      <div id=\"listBox\"> \n" +
                "       <div class=\"listContent\"> \n" +
                "        <dl class=\"element\"> \n" +
                "         <dt> \n" +
                "          <h4 class=\"f14 fontB\"> <i class=\"icon-calendar-color\"></i> 16주차 &nbsp;&nbsp; 2022-08-29 ~ 2022-12-16 <span class=\"btn mini fontHelvetica fwb\" style=\"margin-left:10px;\" href=\"#\" onclick=\"contentsInfoPop('LESN_2208291538367f830101');return false;\">학습목표보기</span> </h4> \n" +
                "         </dt> <!-- 학습요소 START --> \n" +
                "         <dd> \n" +
                "          <div class=\"cont\"> \n" +
                "           <table class=\"boardListBasic\" summary=\"교안관리 주차 리스트입니다.\"> \n" +
                "            <caption>\n" +
                "             교안관리 리스트\n" +
                "            </caption> \n" +
                "            <thead> \n" +
                "             <tr> \n" +
                "              <th class=\"num\">번 호</th> \n" +
                "              <th>구 분</th> \n" +
                "              <th>차시명</th> \n" +
                "              <th>학습현황</th> \n" +
                "              <th> 학습시간 / 수업일자 </th> <!-- \t<th>통과 점수</th> --> \n" +
                "              <th>출결 정보(기간 내)</th> \n" +
                "              <th>출결 정보(기간 후)</th> \n" +
                "              <th>학습 하기</th> \n" +
                "             </tr> \n" +
                "            </thead> \n" +
                "           </table> \n" +
                "          </div> \n" +
                "         </dd> <!-- 학습요소 END --> \n" +
                "        </dl> \n" +
                "       </div> \n" +
                "      </div> <iframe name=\"hiddenFrame\" width=\"0\" height=\"0\" frameborder=\"0\" title=\"파일다운로드프레임\"></iframe> \n" +
                "     </form> \n" +
                "    </div> \n" +
                "   </div> <!-- 본문 종료 --> <!-- 하단 bottom 부분 --> \n" +
                "\n" +
                " </body>\n" +
                "</html>\n";
    }
}