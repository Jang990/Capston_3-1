package com.esummary.elearning.service.vue;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.LectureData;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.subject.NoticeInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.NoticeInfoRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.crawling.SubjectCrawlingService;
import com.esummary.elearning.service.crawling.lecture.LectureCrawlingService;
import com.esummary.elearning.service.crawling.notice.NoticeCrawlingService;
import com.esummary.elearning.service.crawling.task.TaskCrawlingService;
import com.esummary.elearning.service.crawling.week.WeekCrawlingService;
import com.esummary.elearning.service.dao.DBSubjectUtil;
import com.esummary.elearning.service.dao.DBUserSubjectUtil;
import com.esummary.elearning.service.dao.lectures.DBLectureWeekUtil;
import com.esummary.elearning.service.dao.lectures.lecture.DBLectureUtil;
import com.esummary.elearning.service.dao.notice.DBNoticeUtil;
import com.esummary.elearning.service.dao.task.DBTaskUtil;
import com.esummary.elearning.service.dao.user.DBUserInfoUtil;
import com.esummary.elearning.service.dao.user.DBUserLectureUtil;
import com.esummary.elearning.service.dao.user.DBUserTaskUtil;

@Service
public class VueServiceImpl implements VueService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private NoticeInfoRepository subjectNoticeRepository;
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	@Autowired
	private UserLectureRepository userLectureRepository;
	
//	@Autowired
//	private ELearningService eLearningService;
	@Autowired
	private SubjectCrawlingService subjectUtil;
	@Autowired
	private NoticeCrawlingService noticeUtil;
	@Autowired
	private TaskCrawlingService taskUtil;
	@Autowired
	private WeekCrawlingService weekUtil;
	
	@Autowired
	private DBSubjectUtil dbSubjectUtil;
	@Autowired
	private DBLectureWeekUtil dbWeekUtil;
	@Autowired
	private DBLectureUtil dbLectureUtil;
	@Autowired
	private DBNoticeUtil dbNoticeUtil;
	@Autowired
	private DBTaskUtil dbTaskUtil;
	
	@Autowired
	private DBUserSubjectUtil dbUserSubjectUtil;
	@Autowired
	private DBUserLectureUtil dbUserLectureUtil;
	@Autowired
	private DBUserTaskUtil dbUserTaskUtil;
	@Autowired
	private DBUserInfoUtil dbUserInfoUtil;
	
//	@Override
//	public List<SubjectCardData> getInitCardData(String studentNumber) {
//		UserInfo user = userRepository.findWithUserSubjectsByStudentNumber(studentNumber);
//		List<SubjectCardData> cardList = new ArrayList<>();
//		
//		for (UserSubject userSubject : user.getUserSubjects()) {
//			SubjectInfo subject = userSubject.getSubjectInfo();
//			SubjectCardData card = new SubjectCardData(
//					subject.getSubjectId(), subject.getSubjectName(), subject.getSubjectOwnerName());
//			cardList.add(card);
//		}
//		
//		if(cardList.size() > 0) return cardList;
//		else return null;
//	}
	
	public List<NoticeData> getNoticeData(String subjectId) {
		List<NoticeInfo> noticeInfo = subjectNoticeRepository.findBySubjectInfo_SubjectId(subjectId);
		if(noticeInfo == null || noticeInfo.size() == 0) return null;
		
		List<NoticeData> noticeDTO = new ArrayList<>();
		for (NoticeInfo subjectNoticeInfo : noticeInfo) {
			NoticeData notice = new NoticeData(
					subjectNoticeInfo.getNoticeId(),
					subjectNoticeInfo.getTitle(), 
					subjectNoticeInfo.getDescription(),
					subjectNoticeInfo.getAuthor(), 
					subjectNoticeInfo.getCreateDate()
				);  
			noticeDTO.add(notice); 
		}
		
		if(noticeDTO.size() > 0) return noticeDTO;
		else return null;
	}

	@Override
	public boolean saveUser(UserData user) {
		if(userRepository.findByStudentNumber(user.getStudentNumber()).isEmpty()) {
			UserInfo userInfo = new UserInfo();
			userInfo.setStudentNumber(user.getStudentNumber());
			userInfo.setUserName(user.getUserName());
			userRepository.save(userInfo); 
			return true;
		}
		else
			return false;
	}

	@Override
	public List<NoticeData> crawlNotice(UserData user, String subjectId) {
		//크롤링
		List<NoticeInfo> notices = noticeUtil.getSubjectNoticeInfo(subjectId, user.getInitialCookies());
		
		//저장
		dbNoticeUtil.saveService(notices);
		
		//DTO로 변환
		List<NoticeData> noticeDTO = new ArrayList<NoticeData>();
		for (NoticeInfo subjectNoticeInfo : notices) {
			noticeDTO.add(this.convertNoticeData(subjectNoticeInfo));
		}
		
		return noticeDTO;
	}
	
	@Override
	public List<TaskData> crawlTask(UserData user, String subjectId) {
		//크롤링
		List<TaskInfo> tasks = taskUtil.getSubjectTaskInfo(subjectId, user.getInitialCookies());
		
		//저장
		dbTaskUtil.saveService(tasks); // SubjectTask 저장
		Optional<UserSubject> userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentNumber());
		if(userSubject.isEmpty()) System.out.println("여기발생");
		List<UserTask> userTasks = convertToUserTaskList(userSubject.get(), tasks);
		dbUserTaskUtil.saveService(userTasks); //UserTask 저장
		
		//DTO로 변환
		List<TaskData> taskDTO = new ArrayList<TaskData>();
		for (TaskInfo subjectTaskInfo : tasks) {
			taskDTO.add(this.convertTaskData(subjectTaskInfo));
		}
		return taskDTO;
	}
	
	private List<UserTask> convertToUserTaskList(UserSubject userSubject, List<TaskInfo> tasks) {
		List<UserTask> userTasks = new ArrayList<UserTask>();
		for (TaskInfo lecture : tasks) {
			userTasks.add(new UserTask(lecture, userSubject));
		}
		return userTasks;
	}

	@Override
	public List<LectureWeekData> crawlLecture(UserData user, String subjectId) {
//		UserSubject userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentNumber()); 
		//크롤링하기.
		List<WeekInfo> weeks = weekUtil.getSubjectLectureWeekInfo(subjectId, user.getInitialCookies());
		
		//Lecture저장하기.
		dbWeekUtil.saveService(weeks);
		Optional<UserSubject> userSubject = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentNumber());
//		if(userSubject.isEmpty()) System.out.println("UserSubject 존재하지 않음");
		for (WeekInfo week : weeks) {
			dbLectureUtil.saveService(week.getLectures());
			List<UserLecture> userLectures = convertToUserLectureList(userSubject.get(), week.getLectures());
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

	private NoticeData convertNoticeData(NoticeInfo subjectNoticeInfo) {
		return new NoticeData(
				subjectNoticeInfo.getNoticeId(), 
				subjectNoticeInfo.getTitle(), 
				subjectNoticeInfo.getDescription(), 
				subjectNoticeInfo.getAuthor(), 
				subjectNoticeInfo.getCreateDate()
		);
	}
	
	private TaskData convertTaskData(TaskInfo subjectTaskInfo) {
		String startDate =makeDateString(subjectTaskInfo.getStartDate());
		String endDate =makeDateString(subjectTaskInfo.getEndDate());
		
		return new TaskData(
				subjectTaskInfo.getTaskId(),
				subjectTaskInfo.getTitle(),
				subjectTaskInfo.getDescription(),
				startDate,
				endDate,
				subjectTaskInfo.getSubmissionNum(),
				subjectTaskInfo.getNotSubmittedNum(),
				subjectTaskInfo.getTotalNum(),
				subjectTaskInfo.getSubmitYN()
		);
	}

	private String makeDateString(Date startDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DATE)
		+ " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
	}

	@Override
	public List<LectureWeekData> getLectureData(UserData user, String subjectId) {
		String studentNumber = user.getStudentNumber();
		Optional<UserSubject> us = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentNumber());		
		 //조인할 데이터가 아예 없어서 여기 아래로 내려가질 못함. 크롤링을 db구성이 끝나고 해야함 
		if(us.isEmpty()) {
			//기존 데이터가 없음.
			return null;
		}
		System.out.println("강의명: "+ us.get().getSubjectInfo().getSubjectName());
		
		if(us.get().getSubjectInfo().getLectureList().size() == 0) {
			System.out.println("강의없음");
			return null;
		}
//		dbSubjectUtil;
		
		List<LectureWeekData> weekDTO = new ArrayList<LectureWeekData>();
		List<WeekInfo> weekList = us.get().getSubjectInfo().getLectureList();
		for (int i = 0; i < weekList.size(); i++) {
			WeekInfo weekInfo = weekList.get(i);
			List<LectureInfo> lectureDetail = weekInfo.getLectures();
			for (LectureInfo subjectLecture : lectureDetail) {
				UserLecture ul = userLectureRepository.findBySubjectLecture_LectureId(subjectLecture.getLectureId());
				if(ul == null) continue;
				
				subjectLecture.setLearningTime(ul.getLearningTime());
				subjectLecture.setStatus(ul.getStatus()); 
				//여기까지 수정
				//ul. 즉, db에 사용자 lecture데이터가 없을 때 파괴적인 상황을 생각해보기. showCompleted같은 변수들 ++가 잘못되서 차트 싱크가 안맞고 등등... 
			}
			weekDTO.add(new LectureWeekData(weekInfo));
		}
//		for (SubjectLectureWeekInfo weekInfo : weekList) {
//			weekDTO.add(convertWeekData(weekInfo));   
//		}
		return weekDTO;
	}

	@Override
	public List<TaskData> getTaskData(UserData user, String subjectId) {
		String studentNumber = user.getStudentNumber();
		Optional<UserSubject> us = userSubjectRepository.
				findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
		if(us.isEmpty()|| us.get().getUserTask().size() == 0) return null;
		
		List<TaskData> taskDTO = new ArrayList<TaskData>(); 
		List<UserTask> ut = us.get().getUserTask();  
		for (UserTask userTask : ut) {           
			taskDTO.add(convertTaskData(userTask));
		}
		
		return taskDTO;
	}

	private TaskData convertTaskData(UserTask userTask) {
		TaskInfo task = userTask.getSubjectTaskInfo();
		String startDate = makeDateString(task.getStartDate());
		String endDate = makeDateString(task.getEndDate());
		return new TaskData(
				task.getTaskId(), task.getTitle(), task.getDescription(), 
				startDate, endDate, task.getSubmissionNum(), 
				task.getNotSubmittedNum(), task.getTotalNum(), task.getSubmitYN()
			);
	}
	
	@Override
	public InitalPageData crawlInitDataService(UserData userDTO) {
		List<SubjectInfo> crawledBasicSubjectData = crawlInitData(userDTO); //크롤링
		if(crawledBasicSubjectData == null) return null;
		saveInitData(userDTO, crawledBasicSubjectData); //UserSubject와 Subject 저장
		
		InitalPageData initPageData = new InitalPageData(
					crawledBasicSubjectData, userDTO.getUserName(), userDTO.getStudentNumber()
				);
		return initPageData;
	}

	@Override
	public boolean saveUserService(UserData userData) {
		UserInfo user = new UserInfo(userData);
		return dbUserInfoUtil.saveUserService(user);
	}
	
	private List<SubjectInfo> crawlInitData(UserData userDTO) {
		List<SubjectInfo> crawlingBasicSubjectData = subjectUtil.crawlBasicSubjectInfo(userDTO.getInitialCookies()); //크롤링 정보 가져오기
		if(crawlingBasicSubjectData.isEmpty() || crawlingBasicSubjectData == null) return null;
		else return crawlingBasicSubjectData;
	}
	
	//UserSubject와 Subject 저장
	private boolean saveInitData(UserData userDTO, List<SubjectInfo> crawledBasicSubjectData) {
		UserInfo userEntity = new UserInfo(userDTO);
		dbSubjectUtil.saveService(crawledBasicSubjectData);
		List<UserSubject> usList = new ArrayList<UserSubject>();
		for (SubjectInfo subjectInfo : crawledBasicSubjectData) {
			usList.add(new UserSubject(userEntity, subjectInfo));
		}
		dbUserSubjectUtil.saveService(usList);
		
		return true;
	}
	
}
