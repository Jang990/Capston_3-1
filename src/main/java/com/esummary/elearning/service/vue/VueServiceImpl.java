package com.esummary.elearning.service.vue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.LectureData;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.subject.ELearningService;
import com.esummary.elearning.service.subject.util.crawling.lectures.LectureWeekUtil;
import com.esummary.elearning.service.subject.util.crawling.lectures.lecture.LectureUtil;
import com.esummary.elearning.service.subject.util.crawling.notice.NoticeUtil;
import com.esummary.elearning.service.subject.util.crawling.task.TaskUtil;

@Service
public class VueServiceImpl implements VueService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private SubjectNoticeRepository subjectNoticeRepository;
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	@Autowired
	private UserLectureRepository userLectureRepository;
	
	@Autowired
	private ELearningService eLearningService;
	@Autowired
	private NoticeUtil noticeUtil;
	@Autowired
	private TaskUtil taskUtil;
	@Autowired
	private LectureWeekUtil lectureWeekUtil;
	
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
		List<SubjectNoticeInfo> noticeInfo = subjectNoticeRepository.findBySubjectInfo_SubjectId(subjectId);
		if(noticeInfo == null || noticeInfo.size() == 0) return null;
		
		List<NoticeData> noticeDTO = new ArrayList<>();
		for (SubjectNoticeInfo subjectNoticeInfo : noticeInfo) {
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
	public List<TaskData> getTaskData(String subjectId, String studentNumber) {
		UserSubject us = userSubjectRepository.
				findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
		if(us == null || us.getUserTask().size() == 0) return null;
		
		List<TaskData> taskDTO = new ArrayList<TaskData>(); 
		List<UserTask> ut = us.getUserTask();  
		for (UserTask userTask : ut) {           
			taskDTO.add(convertTaskData(userTask));
		}
		
		return taskDTO;
	}

	private TaskData convertTaskData(UserTask userTask) {
		SubjectTaskInfo task = userTask.getSubjectTaskInfo();
		String startDate = makeDateString(task.getStartDate());
		String endDate = makeDateString(task.getEndDate());
		return new TaskData(
				task.getTaskId(), task.getTitle(), task.getDescription(), 
				startDate, endDate, task.getSubmissionNum(), 
				task.getNotSubmittedNum(), task.getTotalNum(), task.getSubmitYN()
			);
	}                   

	private LectureWeekData convertWeekData(SubjectLectureWeekInfo lectureWeekInfo) {
		int cntCompleted = 0;
		int cntIncompleted = 0;
		List<SubjectLecture> lectureDetail = lectureWeekInfo.getLectures();
		List<LectureData> lectureDetailDTO = new ArrayList<LectureData>();
		String startDate =makeDateString(lectureWeekInfo.getStartDate());
		String endDate =makeDateString(lectureWeekInfo.getEndDate());
		
		for (SubjectLecture detail : lectureDetail) {
			
			LectureData lecture = convertLectureData(detail);
			if(isCompletedLecture(lecture)) cntCompleted++;
			else cntIncompleted++;
			
			lectureDetailDTO.add(lecture);
		}
		
		int studyingState = 0;
		if(cntCompleted != 0 || cntIncompleted != 0) {
			studyingState = (int)((float)cntCompleted / (cntCompleted + cntIncompleted) * 100); 
		}
		
		return new LectureWeekData(
				lectureWeekInfo.getLectureWeekId(),    
				lectureWeekInfo.getTitle(), 
				startDate,
				endDate,     
				lectureDetailDTO,
				cntCompleted,
				cntIncompleted,
				studyingState
		);
	}

	private LectureData convertLectureData(SubjectLecture detail) {
		return new LectureData(
				detail.getLectureId(), 
				detail.getLectureVideoId(), 
				detail.getType(), 
				detail.getIdx(), 
				detail.getTitle(), 
				detail.getFullTime(), 
				detail.getStatus(), 
				detail.getLearningTime()
			);
	}

	@Override
	public List<UserSubject> searchUserSubject(String studentNumber) {
		return userSubjectRepository.findByUserInfo_StudentNumber(studentNumber);
	}
	
	@Override
	public boolean saveUser(UserData user) {
		if(userRepository.findByStudentNumber(user.getStudentNumber()) == null) {
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
		UserSubject userSubject = userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, user.getStudentNumber());
		List<SubjectNoticeInfo> notices = noticeUtil.getSubjectNoticeInfo(userSubject, user.getInitialCookies());
		List<NoticeData> noticeDTO = new ArrayList<NoticeData>();
		for (SubjectNoticeInfo subjectNoticeInfo : notices) {
			noticeDTO.add(this.convertNoticeData(subjectNoticeInfo));
		}
		
		return noticeDTO;
	}
	
	@Override
	public List<TaskData> crawlTask(UserData user, String subjectId) {
		UserSubject userSubject = userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, user.getStudentNumber());
		List<SubjectTaskInfo> task = taskUtil.getSubjectTaskInfo(userSubject, user.getInitialCookies());
		List<TaskData> taskDTO = new ArrayList<TaskData>();
		
		for (SubjectTaskInfo subjectTaskInfo : task) {
			taskDTO.add(this.convertTaskData(subjectTaskInfo));
		}
		return taskDTO;
	}
	
	@Override
	public List<LectureWeekData> crawlLecture(UserData user, String subjectId) {
		UserSubject userSubject = userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, user.getStudentNumber());
		List<SubjectLectureWeekInfo> lectures = lectureWeekUtil.getSubjectLectureWeekInfo(userSubject, user.getInitialCookies());
		List<LectureWeekData> lecturesDTO = new ArrayList<LectureWeekData>();
		for (SubjectLectureWeekInfo subjectLectureWeekInfo : lectures) {
			lecturesDTO.add(this.convertWeekData(subjectLectureWeekInfo));
		}     
		
		return lecturesDTO;
	}

	private NoticeData convertNoticeData(SubjectNoticeInfo subjectNoticeInfo) {
		return new NoticeData(
				subjectNoticeInfo.getNoticeId(), 
				subjectNoticeInfo.getTitle(), 
				subjectNoticeInfo.getDescription(), 
				subjectNoticeInfo.getAuthor(), 
				subjectNoticeInfo.getCreateDate()
		);
	}
	
	private TaskData convertTaskData(SubjectTaskInfo subjectTaskInfo) {
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
	
	
	
	private boolean isCompletedLecture(LectureData lecture) {
		if(lecture.getFullTime() == null) return true; //수업 시간이 없다면 해야할 일이 아니다. 화상강의는 알아해라
		System.out.println("오류제어 ======== " + lecture);
		int fullTime = getMinuteNumber(lecture.getFullTime());
		int studyTime = getMinuteNumber(lecture.getLearningTime());
		if(fullTime <= studyTime && lecture.getStatus().equals("1")) return true;
		return false;
	} 

	private int getMinuteNumber(String minuteString) {
		if(!minuteString.contains("분")) return 0;
		return Integer.parseInt(minuteString.substring(0, minuteString.indexOf("분")));
	}  

	@Override
	public List<LectureWeekData> getLectureData(String subjectId, String studentNumber) {
		UserSubject us = userSubjectRepository.
				findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
		
		//조인할 데이터가 아예 없어서 여기 아래로 내려가질 못함. 크롤링을 db구성이 끝나고 해야함 
		if(us == null) {
			return null;
		}
		System.out.println("강의명: "+ us.getSubjectInfo().getSubjectName());
		
		if(us.getSubjectInfo().getLectureList().size() == 0) {
			System.out.println("강의없음");
			return null;
		}
		
		
		List<LectureWeekData> weekDTO = new ArrayList<LectureWeekData>();
		List<SubjectLectureWeekInfo> weekList = us.getSubjectInfo().getLectureList();
		for (int i = 0; i < weekList.size(); i++) {
			SubjectLectureWeekInfo weekInfo = weekList.get(i);
			List<SubjectLecture> lectureDetail = weekInfo.getLectures();
			for (SubjectLecture subjectLecture : lectureDetail) {
				UserLecture ul = userLectureRepository.findBySubjectLecture_LectureId(subjectLecture.getLectureId());
				if(ul == null) continue;
				
				subjectLecture.setLearningTime(ul.getLearningTime());
				subjectLecture.setStatus(ul.getStatus()); 
				//여기까지 수정
				//ul. 즉, db에 사용자 lecture데이터가 없을 때 파괴적인 상황을 생각해보기. showCompleted같은 변수들 ++가 잘못되서 차트 싱크가 안맞고 등등... 
			}
			weekDTO.add(convertWeekData(weekInfo));
		}
//		for (SubjectLectureWeekInfo weekInfo : weekList) {
//			weekDTO.add(convertWeekData(weekInfo));   
//		}
		return weekDTO;
	}
	
	/**
	 * DB에서 가져온 List<SubjectInfo>를  SubjectCardData DTO로 바꿔줌
	 */
	@Override
	public List<SubjectCardData> getSubjectDTO(List<SubjectInfo> subjects) {
		List<SubjectCardData> subjectCards = new ArrayList<SubjectCardData>();
		for (SubjectInfo subjectInfo : subjects) {
			subjectCards.add(new SubjectCardData(
					subjectInfo.getSubjectId(), 
					subjectInfo.getSubjectName(), 
					subjectInfo.getSubjectOwnerName()
				)
			);
		}
	
		return subjectCards;
	}

	@Override
	public InitalPageData crawlInitData(UserData user) {
		InitalPageData initPageData = new InitalPageData();
		String studentName = user.getUserName();
		String studentNumber = user.getStudentNumber();
		
		initPageData.setName(studentName);
		initPageData.setStudentNumber(studentNumber);
		
		List<SubjectInfo> crawlingSubjects = eLearningService.crawlAndSaveBasicSubjectData(user); //크롤링 정보 가져오기
		List<UserSubject> dbUserSubject = this.searchUserSubject(studentNumber); //기존에 있던 과목정보
		this.saveUser(user); //User테이블에 정보가 없으면 저장
		
		//분기가 필요하다. 기존 db에 데이터가 있는 유저거나, db에 데이터가 없는 유저거나.
		if(dbUserSubject.isEmpty()) {
			//DB에 저장된 데이터가 전혀 없을때 전부 크롤링해서 db에 저장함
			eLearningService.saveBasicSubjectData(user, crawlingSubjects); //크롤링 정보 저장
		}
		else {
			List<SubjectInfo> needStoredSubjects = getNeedStoredSubjectData(dbUserSubject, crawlingSubjects);
			eLearningService.saveBasicSubjectData(user, needStoredSubjects); //필요한 사용자의 과목정보 정보 저장
		}
		
		initPageData.setSubjectCardData(this.getSubjectDTO(crawlingSubjects));
//		initPageData.setSubjectCardData(this.getInitCardData(studentNumber));
		return initPageData;
	}
	
	private List<SubjectInfo> getNeedStoredSubjectData(List<UserSubject> dbUserSubject,
			List<SubjectInfo> crawlingSubjects) {   
		List<SubjectInfo> needStoredSubjects = new ArrayList<SubjectInfo>();
		
		String[] dbSubjectId = new String[dbUserSubject.size()];
		String[] crawlingSubjectId = new String[crawlingSubjects.size()];
		
		int i = 0;
		for (UserSubject userSubject : dbUserSubject) {
			dbSubjectId[i] = userSubject.getSubjectId();
			i++;
		}
		
		i = 0;
		for (SubjectInfo subjectInfo : crawlingSubjects) {
			crawlingSubjectId[i] = subjectInfo.getSubjectId();
			i++;
		}
		
		for (int j = 0; j < crawlingSubjectId.length; j++) {
			if(Arrays.asList(dbSubjectId).contains(crawlingSubjectId[j])) {
				continue;
			}
			else { 
				needStoredSubjects.add(crawlingSubjects.get(j));
			}
		}
		
		if(needStoredSubjects.size() == 0) {
			return null;
		}
		else {
			System.out.println(needStoredSubjects.toString());
			return needStoredSubjects;
		}
	}
	
}
