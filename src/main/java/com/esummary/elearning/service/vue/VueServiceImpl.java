package com.esummary.elearning.service.vue;

import java.util.ArrayList; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.NoticeData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.dto.TaskData;
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectNoticeRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.subject.util.crawling.notice.NoticeUtil;

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
	
	@Override
	public List<SubjectCardData> getInitCardData(String studentNumber) {
		UserInfo user = userRepository.findWithUserSubjectsByStudentNumber(studentNumber);
		List<SubjectCardData> cardList = new ArrayList<>();
		
		for (UserSubject userSubject : user.getUserSubjects()) {
			SubjectInfo subject = userSubject.getSubjectInfo();
			SubjectCardData card = new SubjectCardData(
					subject.getSubjectId(), subject.getSubjectName(), subject.getSubjectOwnerName());
			cardList.add(card);
		}
		
		if(cardList.size() > 0) return cardList;
		else return null;
	}
	
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
		return new TaskData(
				task.getTaskId(), task.getTitle(), task.getDescription(), 
				task.getStartDate(), task.getEndDate(), task.getSubmissionNum(), 
				task.getNotSubmittedNum(), task.getTotalNum(), task.getSubmitYN()
			);
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

	private LectureWeekData createWeekData(SubjectLectureWeekInfo weekInfo) {
		return new LectureWeekData(
				weekInfo.getLectureWeekId(), weekInfo.getTitle(), 
				weekInfo.getStartDate(), weekInfo.getEndDate()
		);
	}

	@Override
	public boolean isExistUserSubjectInDB(String studentNumber) {
		boolean check =(userSubjectRepository.findByUserInfo_StudentNumber(studentNumber).isEmpty()) ? false : true;
		return check;
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
	
}
