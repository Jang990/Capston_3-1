package com.esummary.crawling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.auth.exception.DeniedElearningCookieException;
import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.InhatcUserDTO;
import com.esummary.crawling.dto.NoticeData;
import com.esummary.crawling.dto.TaskData;
import com.esummary.crawling.dto.exInitalPageData;
import com.esummary.crawling.dto.exSubjectCardData;
import com.esummary.elearning.dao.DBSubjectUtil;
import com.esummary.elearning.dao.DBUserSubjectUtil;
import com.esummary.elearning.dao.lectures.DBLectureWeekUtil;
import com.esummary.elearning.dao.lectures.lecture.DBLectureUtil;
import com.esummary.elearning.dao.notice.DBNoticeUtil;
import com.esummary.elearning.dao.task.DBTaskUtil;
import com.esummary.elearning.dao.user.DBUserLectureUtil;
import com.esummary.elearning.dao.user.DBUserTaskUtil;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.exdto.subject.LectureWeekData;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.elearning.exservice.crawling.SubjectCrawlingService;
import com.esummary.elearning.exservice.crawling.notice.NoticeCrawlingService;
import com.esummary.elearning.exservice.crawling.task.TaskCrawlingService;
import com.esummary.elearning.exservice.crawling.week.WeekCrawlingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InhatcCrawlingService implements CrawlingService {
	
	// crawlLoginPage 사용
	private final SubjectCrawlingService subjectUtil;
	private final DBSubjectUtil dbSubjectUtil;
	private final DBUserSubjectUtil dbUserSubjectUtil;
	
	// crawlTask 사용
	private final TaskCrawlingService taskUtil;
	private final DBTaskUtil dbTaskUtil;
	private final DBUserTaskUtil dbUserTaskUtil;
	
	// crawlNotice 사용
	private final NoticeCrawlingService noticeUtil;
	private final DBNoticeUtil dbNoticeUtil;
	
	//crawlLecture 사용
	private final DBLectureWeekUtil dbWeekUtil;
	private final DBLectureUtil dbLectureUtil;
	private final DBUserLectureUtil dbUserLectureUtil;
	private final WeekCrawlingService weekUtil;
	
	
	@Override
	public List<InhatcSubjectCardDTO> crawlLoginPage(InhatcUserDTO userDTO) {
		
		//크롤링 정보 가져오기
		List<SubjectInfo> basicSubjectData = subjectUtil.crawlBasicSubjectInfo(userDTO.getInitialCookies());
		
		if(basicSubjectData.isEmpty() || basicSubjectData == null) 
			throw new DeniedElearningCookieException("만료된 이러닝 로그인 쿠키 or 이러닝 사이트 다운"); // 크롤링 실패
		
		//UserSubject와 Subject 저장
		UserInfo userEntity = InhatcUserDTO.toEntity(userDTO);
		dbSubjectUtil.saveService(basicSubjectData);
		List<UserSubject> usList = new ArrayList<UserSubject>();
		for (SubjectInfo subjectInfo : basicSubjectData) {
			usList.add(new UserSubject(userEntity, subjectInfo));
		}
		dbUserSubjectUtil.saveService(usList);
		
		List<InhatcSubjectCardDTO> subjectCards = new ArrayList<>();
		for (SubjectInfo subject : basicSubjectData) {
			subjectCards.add(InhatcSubjectCardDTO.from(subject));
		}
		
		return subjectCards;
	}
	
	@Override
	public List<TaskData> crawlTask(InhatcUserDTO user, String subjectId) {
		//크롤링
		List<TaskInfo> tasks = taskUtil.getSubjectTaskInfo(subjectId, user.getInitialCookies());
		
		//저장
		dbTaskUtil.saveService(tasks); // SubjectTask 저장
		UserSubject userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentId())
				.orElseThrow(() -> new IllegalArgumentException("해당 정보가 없습니다. -> 과목ID: "+subjectId+", 사용자ID: "+user.getStudentId()));
		
		//UserTask로 변환
		List<UserTask> userTasks = new ArrayList<UserTask>();
		for (TaskInfo lecture : tasks) {
			userTasks.add(new UserTask(lecture, userSubject));
		}
		
		dbUserTaskUtil.saveService(userTasks); //UserTask 저장
		
		//DTO로 변환
		List<TaskData> taskDTO = new ArrayList<TaskData>();
		for (TaskInfo subjectTaskInfo : tasks) {
			taskDTO.add(TaskData.from(subjectTaskInfo));
		}
		return taskDTO;
	}

	@Override
	public List<NoticeData> crawlNotice(InhatcUserDTO userDto, String subjectId) {
		//크롤링
		List<NoticeInfo> notices = noticeUtil.getSubjectNoticeInfo(subjectId, userDto.getInitialCookies());
		
		//저장
		dbNoticeUtil.saveService(notices);
		
		//DTO로 변환
		List<NoticeData> noticeDTO = new ArrayList<NoticeData>();
		for (NoticeInfo subjectNoticeInfo : notices) {
			noticeDTO.add(NoticeData.from(subjectNoticeInfo));
		}
		
		return noticeDTO;
	}

	//이 부분은 너무 복잡해서 일단 로직만 작동하게 놔둔다.
	@Override
	public List<LectureWeekData> crawlLecture(InhatcUserDTO userDto, String subjectId) {
		//크롤링하기.
		List<WeekInfo> weeks = weekUtil.getSubjectLectureWeekInfo(subjectId, userDto.getInitialCookies());
		
		//Lecture저장하기.
		dbWeekUtil.saveService(weeks);
		UserSubject userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, userDto.getStudentId())
				.orElseThrow(() -> new IllegalArgumentException("해당 정보가 없습니다. -> 과목ID: "+subjectId+", 사용자ID: "+userDto.getStudentId()));
		
		for (WeekInfo week : weeks) {
			dbLectureUtil.saveService(week.getLectures());
			List<UserLecture> userLectures = convertToUserLectureList(userSubject, week.getLectures());
			dbUserLectureUtil.saveService(userLectures);
		}
		
		//DTO로 바꾸기.
		List<LectureWeekData> lecturesDTO = new ArrayList<LectureWeekData>();
		for (WeekInfo subjectLectureWeekInfo : weeks) {
			lecturesDTO.add(new LectureWeekData(subjectLectureWeekInfo));
		}
		
		return lecturesDTO;
	}
	
	private List<UserLecture> convertToUserLectureList(UserSubject userSubject, List<LectureInfo> lectureList) {
		/*
		 *  예외사례
		 * 사용자 A의 첫 사이트 이용인 경우 saveService부분에서 userLecture에 ul_Id가 무조건 들어간다.
		 * 근데 사용자 A가 재접속을 한다면? save를 하지않음 -> setLectureId를 하지않음 -> 즉 ul_Id는 null이 된다.
		 *  
		 *  해결방안
		 * convertToUserLectureList 부분에서 SubjectLecture를 검색해서 넣어준다.
		 */
		List<UserLecture> userLectures = new ArrayList<UserLecture>();
		for (LectureInfo lecture : lectureList) {
			if(lecture.getLectureId() == null) { 
				Optional<LectureInfo> dbLecture = dbLectureUtil.getLecture(lecture.getWeekId(), lecture.getIdx()); //여기 최적화 필요. 전부 쿼리를 보냄. 한번에 불러오고 키값으로 찾는게 좋을 듯
				if(dbLecture.isEmpty()) System.out.println("======= 오류위치: convertToUserLectureList ");
				else {
					lecture.setLectureIdForCrawlingObject(dbLecture.get().getLectureId()); // 크롤링을 했기때문에 MySQL에 ID값은 들어가 있지 않다.
				}
			}
			userLectures.add(new UserLecture(lecture, userSubject));
		}
		return userLectures;
	}

}
