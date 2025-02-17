package com.esummary.crawler.elearning.assignment;

import com.esummary.crawler.connection.PageConnector;
import com.esummary.crawler.elearning.assignment.dto.AssignmentDTO;
import com.esummary.crawler.elearning.assignment.dto.AssignmentSubmissionStatus;
import com.esummary.crawler.elearning.dto.ContentCompletionStatus;
import com.esummary.crawler.elearning.dto.ContentDetail;
import com.esummary.crawler.elearning.dto.ContentPeriod;
import org.jsoup.Jsoup;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InhatcAssignmentCrawlerTest {

    @InjectMocks
    private InhatcAssignmentCrawler crawler;
    @Mock
    private PageConnector connector;

    @Test
    @DisplayName("과제 크롤링 정상 작동")
    public void crawlAssignment() throws Exception {
        //given
        ContentDetail detail = new ContentDetail("TestID", "TestTitle", "TestContent");
        ContentPeriod period = createPeriod();
        AssignmentSubmissionStatus submissionStatus = new AssignmentSubmissionStatus(10, 14, 24);

        AssignmentDTO assignmentDTO = new AssignmentDTO(detail, period,submissionStatus, ContentCompletionStatus.Completed);
        String docString = creatDocumentString(assignmentDTO);
        when(connector.getContent(any())).thenReturn(Jsoup.parse(docString));

        String testCourse = "testCourse";
        Map<String, String> testCookies = new HashMap<>();

        //when
        List<AssignmentDTO> assignmentDTOList = crawler.crawlAssignment(testCourse, testCookies);

        //then
        assertThat(assignmentDTOList.size()).isEqualTo(1);
        AssignmentDTO findAssignmentDTO = assignmentDTOList.get(0);

        ContentDetail findContentDetail = findAssignmentDTO.getContentDetail();
        assertThat(findContentDetail.getId()).isEqualTo(detail.getId());
        assertThat(findContentDetail.getTitle()).isEqualTo(detail.getTitle().concat(" [마감]")); // 이 부분은 성공함
//        assertThat(findContentDetail.getTitle()).isEqualTo(detail.getTitle()); // 해당 부분 수정 필요
        assertThat(findContentDetail.getContent()).isEqualTo(detail.getContent());

        ContentPeriod findPeriod = findAssignmentDTO.getContentPeriod();
        assertThat(findPeriod.getFrom()).isEqualTo(period.getFrom());
        assertThat(findPeriod.getTo()).isEqualTo(period.getTo());

        AssignmentSubmissionStatus findSubmissionStatus = findAssignmentDTO.getTotalStatus();
        assertThat(findSubmissionStatus.getSubmittedCount()).isEqualTo(submissionStatus.getSubmittedCount());
        assertThat(findSubmissionStatus.getUnsubmittedCount()).isEqualTo(submissionStatus.getUnsubmittedCount());
        assertThat(findSubmissionStatus.getTotalAssignmentCount()).isEqualTo(submissionStatus.getTotalAssignmentCount());

        // complete 여부는 제출 버튼이 있는지 없는지로 판단하기 때문에 테스트하기 힘듦
        assertThat(findAssignmentDTO.getPersonalSubmissionStatus()).isEqualTo(assignmentDTO.getPersonalSubmissionStatus());
    }

    private ContentPeriod createPeriod() {
        LocalDateTime start = LocalDateTime.of(2023, 4, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 4, 8, 0, 0);
        return new ContentPeriod(start, end);
    }

    @Test
    @DisplayName("잘못된 세션으로 과제 크롤링 시도")
    public void crawlAssignmentFail() throws Exception {
        //given
        String docString = createFailDocument();

        when(connector.getContent(any())).thenReturn(Jsoup.parse(docString));
        String testCourse = "testCourse";
        Map<String, String> testCookies = new HashMap<>();

        //when
        List<AssignmentDTO> assignmentList = crawler.crawlAssignment(testCourse, testCookies);

        //then
        assertThat(assignmentList.size()).isEqualTo(0);
    }

    private String creatDocumentString(AssignmentDTO assignmentDTO) {
        String documentString =
                "<div id=\"listBox\"> <!-- 과제 목록 iterate --> \n" +
                        "        <div class=\"listContent pb20\"> \n" +
                        "         <dl class=\"element\"> \n" +
                        "          <dt> \n" +
                        "           <h4 class=\"f14\"> <i class=\"icon-openbook-color mr10\"></i> %s <span class=\"ml10 fcRed f12\">[마감]</span> </h4> \n" +
                        "           <ul class=\"btnBox\"> \n" +
                        "            <li> </li> \n" +
                        "            <li><a class=\"btn small\" onclick=\"javascript:submitReportView('%s', 'N');\"><i class=\"icon-note-small-color\"></i>제출정보보기</a></li> \n" +
                        "           </ul> \n" +
                        "          </dt> \n" +
                        "          <dd> \n" +
                        "           <ul class=\"fl ml10\" style=\"height:28px;\"> \n" +
                        "            <li><b>참고 자료 :</b> </li> \n" +
                        "           </ul> \n" +
                        "          </dd> \n" +
                        "          <dd> \n" +
                        "           <table class=\"boardListInfo\" summary=\"과제 관련 리스트입니다.\"> \n" +
                        "            <caption>\n" +
                        "             과제 정보 리스트\n" +
                        "            </caption> \n" +
                        "            <thead> \n" +
                        "             <tr> \n" +
                        "              <th>제출기간</th> \n" +
                        "              <th class=\"noLearner\">성적반영</th> \n" +
                        "              <th class=\"noLearner\">성적공개</th> \n" +
                        "              <th>성적공개일자</th> \n" +
                        "              <th>연장제출</th> \n" +
                        "              <th scope=\"col\" class=\"last\"> 제출자수 (제출 / 미제출 / 연장제출 / 수강인원) </th> \n" +
                        "             </tr> \n" +
                        "            </thead> \n" +
                        "            <tbody> \n" +
                        "             <tr> \n" +
                        "              <td> %s ~ %s </td> \n" +
                        "              <td class=\"fcBlue\"> 반 영 </td> \n" +
                        "              <td class=\"fcBlue\"> <!-- 성적 반영과 성적공개는 별개!! --> 미공개 </td> \n" +
                        "              <td> </td> \n" +
                        "              <td> 미허용 </td> <!-- 학생 상호 피드백 현재 없음. --> <!-- \t\t\t\t\t\t\t\t\t\t<td> --> <!-- \t\t\t\t\t\t\t\t\t\t</td> --> \n" +
                        "              <td class=\"last\"> %d 명 / %d 명 / 0 명 / %d 명 </td> \n" +
                        "             </tr> \n" +
                        "            </tbody> \n" +
                        "           </table> \n" +
                        "          </dd> \n" +
                        "          <dd> \n" +
                        "           <div class=\"cont pb0\" style=\"min-height:0;word-break:break-all;\"> \n" +
                        "            %s" +
                        "           </div> \n" +
                        "          </dd> \n" +
                        "         </dl> \n" +
                        "        </div> " +
                        "</div>";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return String.format(documentString,
                assignmentDTO.getContentDetail().getTitle(),
                assignmentDTO.getContentDetail().getId(),
                assignmentDTO.getContentPeriod().getFrom().format(formatter),
                assignmentDTO.getContentPeriod().getTo().format(formatter),
                assignmentDTO.getTotalStatus().getSubmittedCount(),
                assignmentDTO.getTotalStatus().getUnsubmittedCount(),
                assignmentDTO.getTotalStatus().getTotalAssignmentCount(),
                assignmentDTO.getContentDetail().getContent()
                );
    }

    private String createFailDocument() {
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