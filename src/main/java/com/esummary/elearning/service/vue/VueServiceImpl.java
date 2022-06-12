package com.esummary.elearning.service.vue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.subject.util.crawling.lectures.LectureWeekUtil;
import com.esummary.elearning.service.subject.util.crawling.lectures.lecture.LectureUtil;
import com.esummary.elearning.service.subject.util.crawling.notice.NoticeUtil;
import com.esummary.elearning.service.subject.util.crawling.task.TaskUtil;

@Service
public class VueServiceImpl implements VueService {
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired
	SubjectNoticeRepository subjectNoticeRepository;
	@Autowired
	UserSubjectRepository userSubjectRepository; 
	
	@Autowired
	NoticeUtil noticeUtil;
	@Autowired
	TaskUtil taskUtil;
	@Autowired
	LectureWeekUtil lectureWeekUtil;
	
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
		if(us == null) return null;
		
		List<TaskData> taskDTO = new ArrayList<TaskData>(); 
		List<UserTask> ut = us.getUserTask();  
		for (UserTask userTask : ut) {           
			taskDTO.add(craeteTaskData(userTask));
		}
		
		return taskDTO;
	}

	private TaskData craeteTaskData(UserTask userTask) {
		SubjectTaskInfo task = userTask.getSubjectTaskInfo();
		String startDate = makeDateString(task.getStartDate());
		String endDate = makeDateString(task.getEndDate());
		return new TaskData(
				task.getTaskId(), task.getTitle(), task.getDescription(), 
				startDate, endDate, task.getSubmissionNum(), 
				task.getNotSubmittedNum(), task.getTotalNum(), task.getSubmitYN()
			);
	}                   

	private LectureWeekData createWeekData(SubjectLectureWeekInfo weekInfo) {
		//여기 null 고쳐야한다.
		return new LectureWeekData(
				weekInfo.getLectureWeekId(), weekInfo.getTitle(), 
				weekInfo.getStartDate(), weekInfo.getEndDate(), null ,0,0
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
			noticeDTO.add(new NoticeData(
					subjectNoticeInfo.getNoticeId(), 
					subjectNoticeInfo.getTitle(), 
					subjectNoticeInfo.getDescription(), 
					subjectNoticeInfo.getAuthor(), 
					subjectNoticeInfo.getCreateDate())
			);
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
			String startDate =makeDateString(subjectTaskInfo.getStartDate());
			String endDate =makeDateString(subjectTaskInfo.getEndDate());
			
			taskDTO.add(new TaskData(
					subjectTaskInfo.getTaskId(),
					subjectTaskInfo.getTitle(),
					subjectTaskInfo.getDescription(),
					startDate,
					endDate,
					subjectTaskInfo.getSubmissionNum(),
					subjectTaskInfo.getNotSubmittedNum(),
					subjectTaskInfo.getTotalNum(),
					subjectTaskInfo.getSubmitYN()
				)
			);
		}
		return taskDTO;
	}
	
	private String makeDateString(Date startDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DATE)
		+ " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
	}


	@Override
	public List<LectureWeekData> crawlLecture(UserData user, String subjectId) {
		UserSubject userSubject = userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, user.getStudentNumber());
		List<SubjectLectureWeekInfo> lectures = lectureWeekUtil.getSubjectLectureWeekInfo(userSubject, user.getInitialCookies());
		List<LectureWeekData> lecturesDTO = new ArrayList<LectureWeekData>();
		for (SubjectLectureWeekInfo subjectLectureWeekInfo : lectures) {
			int cntCompleted = 0;
			int cntIncompleted = 0;
			List<SubjectLecture> lectureDetail = subjectLectureWeekInfo.getLectures();
			List<LectureData> lectureDetailDTO = new ArrayList<LectureData>();
			for (SubjectLecture detail : lectureDetail) {
				LectureData lecture = new LectureData(
						detail.getLectureId(), 
						detail.getLectureVideoId(), 
						detail.getType(), 
						detail.getIdx(), 
						detail.getTitle(), 
						detail.getFullTime(), 
						detail.getStatus(), 
						detail.getLearningTime()
					);
				
				if(isCompletedLecture(lecture)) cntCompleted++;
				else cntIncompleted++;
				
				lectureDetailDTO.add(lecture);
			}      
					
			lecturesDTO.add(new LectureWeekData(
					subjectLectureWeekInfo.getLectureWeekId(),    
					subjectLectureWeekInfo.getTitle(), 
					subjectLectureWeekInfo.getStartDate(),
					subjectLectureWeekInfo.getEndDate(),
					lectureDetailDTO,
					cntCompleted,
					cntIncompleted
				)
			);  
		}     
		
		return lecturesDTO;
	}
	
	private boolean isCompletedLecture(LectureData lecture) {
		if(lecture.getFullTime() == null) return true; //수업 시간이 없다면 해야할 일이 아니다. 화상강의는 알아해라
		
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
	public List<LectureWeekData> getLectureeData(String subjectId, String studentNumber) {
		UserSubject us = userSubjectRepository.
				findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
		//조인할 데이터가 아예 없어서 여기 아래로 내려가질 못함. 크롤링을 db구성이 끝나고 해야함 
		if(us == null) {
			return null;
		}
		
		List<LectureWeekData> weekDTO = new ArrayList<LectureWeekData>(); 
		List<SubjectLectureWeekInfo> weekList = us.getSubjectInfo().getLectureList();
		for (SubjectLectureWeekInfo weekInfo : weekList) {
			weekDTO.add(createWeekData(weekInfo));   
		}
		return weekDTO;
	}
	
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
	
}
