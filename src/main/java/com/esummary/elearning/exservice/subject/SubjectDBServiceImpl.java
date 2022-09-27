package com.esummary.elearning.exservice.subject;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.esummary.crawling.dto.LectureWeekData;
import com.esummary.crawling.dto.NoticeData;
import com.esummary.crawling.dto.SubjectDetailDataWithCnt_DTO;
import com.esummary.crawling.dto.TaskData;
import com.esummary.elearning.dao.DBUserSubjectUtil;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.TaskInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.entity.user.UserSubject;
import com.esummary.entity.user.UserTask;
import com.esummary.repository.UserSubjectRepository;
import com.esummary.repository.subject.NoticeInfoRepository;
import com.esummary.repository.user.UserLectureRepository;


@Service
public class SubjectDBServiceImpl implements SubjectDBService {
	@Autowired
	private DBUserSubjectUtil dbUserSubjectUtil;
	@Autowired
	private NoticeInfoRepository subjectNoticeRepository;
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	@Autowired
	private UserLectureRepository userLectureRepository;
	
	/**
	 * 사용자 정보(UserData)와 과목ID를 주면 이에 해당하는 과목의 하위 정보(공지, 주차-강의, 과제)내용을 전부 가져옴
	 */
	@Override
	public List<SubjectDetailDataWithCnt_DTO> getSubjectDataWithAllDetail(UserData user, String subjectId) {
		
		return null;
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
		System.out.println(us.get().getSubjectInfo().getLectureList().size());
		if(us.get().getSubjectInfo().getLectureList().size() == 0) {
			System.out.println("강의없음");
			return null;
		}
		
		List<LectureWeekData> weekDTO = new ArrayList<LectureWeekData>();
		List<WeekInfo> weekList = us.get().getSubjectInfo().getLectureList();
		for (int i = 0; i < weekList.size(); i++) {
			WeekInfo weekInfo = weekList.get(i);
			List<LectureInfo> lectureDetail = weekInfo.getLectures(); //에러 발생부분 week정보까지 가져오고 lecture까지는 안가져오기 때문
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
	public List<TaskData> getTaskData(UserData user, String subjectId) {
		String studentNumber = user.getStudentNumber();
		Optional<UserSubject> us = userSubjectRepository.
				findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
		if(us.isEmpty()|| us.get().getUserTask().size() == 0) return null;
		
		List<TaskData> taskDTO = new ArrayList<TaskData>(); 
		List<UserTask> ut = us.get().getUserTask();  
		for (UserTask userTask : ut) {           
			taskDTO.add(TaskData.convertTaskData(userTask));
		}
		
		return taskDTO;
	}
	
	
	
	
	
}
