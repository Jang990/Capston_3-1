package com.esummary.elearning.exservice.crawling.lecture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.exservice.crawling.ELearningURL;
import com.esummary.elearning.exservice.crawling.SubjectCrawlingService_Inhatc;
import com.esummary.elearning.repository.subject.LectureInfoRepository;
import com.esummary.elearning.repository.subject.WeekInfoRepository;
import com.esummary.elearning.repository.subject.TaskInfoRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;

@Component
public class LectureCrawlingService_Inhatc implements LectureCrawlingService {
	
	@Autowired
	private LectureInfoRepository subjectLectureRepository;
	@Autowired
	private UserLectureRepository userLectureRepository;
	
	//리팩토링 중...
	public List<LectureInfo> getLectureList(Elements lectureElements, String lectureWeekId) {
		List<LectureInfo> lectures = new ArrayList<LectureInfo>();
		
		for (Element element : lectureElements) {
			String idx = crawlIdx(element);
			String type = crawlType(element); 
			String title = crawlTitle(element);
			String fullTime = crawlFulltime(element);
			String learningTime = crawlLearningTime(element);
			String status = crawlStatus(element);
			if(status.equals("full")) // full O, empty X, half 세모
				status = "1";
			else
				status = "0";
			
			/*
			 *  viewStudyBoard일 경우 학습활동 글쓰기 이기 때문에 학습하기를 찾아야 함 
			 *  또는 학습활동 글쓰기 id와 교안을 따로 저장해야함.
			 */
			final String jsCodeSelector = "tr > td:nth-child(8) > p > a";
			String jsCode = findStudyVideoId(element.select(jsCodeSelector));
			
			/*
			 * javascript:viewStudyContents(
			 * 'LESN_220301162243a6d4208b','LESN_22030112283496aa7084','1024','768','date','0','1');
			 * 
			 * lessonElementId, = 고유 학습 id
			 * lessonContentsId, = 해당 주차의 id 
			 * windowWidth,
			 * windowHeight, 
			 * learningControl, = 값이 date일때 학습기간(lessonCnt)을 판단함(거의 다 date) 
			 * lessonCnt, = -1일때는 학습기간이 아님, 0일때 학습기간임 
			 * lessonOrder = 몇 주차 번호 (1, 2, 3, 4 ... 15)
			 */
			String[] videoIdAndDetails = SubjectCrawlingService_Inhatc.extractDataFromJsCode(jsCode);//일단 
			
			LectureInfo lecture = new LectureInfo(videoIdAndDetails[0], type, idx, title, fullTime, status, learningTime, lectureWeekId);
//			lecture.setLectureId(seq_Leture++);
//			subjectLectureRepository.save(lecture);
			
			lectures.add(lecture);
			
//			UserLecture ul = new UserLecture(seq_UserLec++, status, learningTime, userSubject, lecture);
//			userLectureRepository.save(ul);
		}
		
		return lectures;
	}
	
	private String crawlLearningTime(Element element) {
		final String learningTimeSelector = "tr > td:nth-child(6) > span";
		return element.select(learningTimeSelector).text().replace("TOTAL : ", "");
	}

	private String crawlFulltime(Element element) {
		final String fullTimeSelector = "tr > td:nth-child(5)";
		return element.select(fullTimeSelector).text();
	}

	private String crawlStatus(Element element) {
		final String statusSelector = "tr > td:nth-child(4) > img";
		String status = element.select(statusSelector).attr("src")
				.replace("/lmsdata/img_common/icon/set1/icon_", "")
				.replace("_print.gif", "").trim();
		return status;
	}

	private String crawlTitle(Element element) {
		final String titleSelector = "tr > td:nth-child(3)";
		return element.select(titleSelector).text();
	}

	private String crawlType(Element element) {
		final String typeSelector = "tr > td:nth-child(2)"; // 굳이 있어야 하나? 
		return element.select(typeSelector).text();
	}

	private String crawlIdx(Element element) {
		final String idxSelector = "tr > td:nth-child(1)";
		return element.select(idxSelector).text();
	}

	private String findStudyVideoId(Elements elements) {
		/*
		 * <a class="btn btn-blue" href="javascript:viewStudyBoard('LESN_220222100448a8b62795','LESN_220222100448a8b62794','study');" rel="tooltip">학습활동 글쓰기</a>
		 * <a class="btn" href="javascript:viewStudyContents('LESN_220222100448a8b62795','LESN_220222100448a8b62794','1024','768','date','0','1');">학습 하기</a>
		 * <a class="btn" onclick="fileDown('*5B*EC*9E*90*EA*B8*B0*EA*B0*9C*EB*B0*9C*EB*8A*A5*EB*A0*A5*5D01*EC*B0*A8*EC*8B*9C_*EA*B0*95*EC*9D*98*EA*B5*90*EC*95*88_*EC*9E*90*EA*B8*B0*EA*B0*9C*EB*B0*9C*EC*9D*98*20*EC*9D*B4*ED*95*B4.pdf','*5B*EC*9E*90*EA*B8*B0*EA*B0*9C*EB*B0*9C*EB*8A*A5*EB*A0*A5*5D01*EC*B0*A8*EC*8B*9C_*EA*B0*95*EC*9D*98*EA*B5*90*EC*95*88_*EC*9E*90*EA*B8*B0*EA*B0*9C*EB*B0*9C*EC*9D*98*20*EC*9D*B4*ED*95*B4.pdf','*2Flmsdata*2Fcontents*2F202113022LLA104*2F*5B*EC*9E*90*EA*B8*B0*EA*B0*9C*EB*B0*9C*EB*8A*A5*EB*A0*A5*5D01*EC*B0*A8*EC*8B*9C_*EA*B0*95*EC*9D*98*EA*B5*90*EC*95*88_*EC*9E*90*EA*B8*B0*EA*B0*9C*EB*B0*9C*EC*9D*98*20*EC*9D*B4*ED*95*B4.pdf');">교안</a>
		 * 이렇게 학습활동 글쓰기, 학습하기, 교안
		 * 여기서 학습하기를 찾아낸다.
		 * 
		 * 교안 같은것을 활용하려면 여기를 조금 바꾸면 사용가능할 듯
		 */
		String studyVideoId = "";
		for (Element element : elements) {
			String btnName = element.text();
			if(btnName.equals("학습 하기")) {
				String jsCode = element.attr("href");
				studyVideoId = SubjectCrawlingService_Inhatc.extractDataFromJsCode(jsCode)[0];
			}
		}
		return studyVideoId;
	}
	
}
