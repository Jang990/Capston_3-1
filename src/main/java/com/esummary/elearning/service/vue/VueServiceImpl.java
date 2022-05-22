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

@Service
public class VueServiceImpl implements VueService {
	
	@Autowired 
	UserRepository userRepository;
	
	@Autowired
	SubjectNoticeRepository subjectNoticeRepository;
	@Autowired
	UserSubjectRepository userSubjectRepository; 
	
	
	
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
		if(us == null) return null;
		
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
		System.out.println("유저체크: "+ check);
		return check;
	}
	
}
