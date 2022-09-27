package com.esummary.subject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.crawling.dto.tofront.TaskData;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.NoticeInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserLecture;
import com.esummary.entity.user.UserSubject;
import com.esummary.entity.user.UserTask;
import com.esummary.repository.UserSubjectRepository;
import com.esummary.repository.subject.NoticeInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserSubjectServiceImpl implements UserSubjectService {
	
	private final UserSubjectRepository userSubjectRepository;
	private final SubjectService subjectService;
	
	@Override
	public void checkUserOwnSubject(String studentId, String subjectId) {
		if(!userSubjectRepository.existsByUserInfo_StudentNumberAndSubjectInfo_SubjectId(subjectId, studentId))
			throw new IllegalArgumentException("사용자가 해당 과목을 가지고 있지 않습니다. 학번 ="+ studentId +", 과목ID ="+subjectId);
	}

	@Override
	public List<NoticeData> getNoticeData(String subjectId) {
		return subjectService.getNoticeData(subjectId);
	}
	
	@Override
	public List<TaskData> getTaskData(String studentId, String subjectId) {
		Optional<UserSubject> us = userSubjectRepository.
				findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentId);
		if(us.isEmpty()|| us.get().getUserTask().size() == 0) return null;
		
		List<TaskData> taskDTO = new ArrayList<TaskData>(); 
		List<UserTask> ut = us.get().getUserTask();  
		for (UserTask userTask : ut) {           
			taskDTO.add(TaskData.from(userTask.getTaskInfo()));
		}
		
		return taskDTO;
	}
	
	@Override
	public List<LectureWeekData> getLectureData(String studentId, String subjectId) {
		UserSubject us = userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 과목이 존재하지 않음. 학번: "+studentId+", 과목ID: "+subjectId));		
		 //조인할 데이터가 아예 없어서 여기 아래로 내려가질 못함. 크롤링을 db구성이 끝나고 해야함 
		
		if(us.getSubjectInfo().getLectureList().size() == 0) {
			throw new IllegalArgumentException("해당 과목의 수업이 존재하지 않음. 과목ID: "+subjectId); // 다른 오류를 정의하는게 좋을까?
		}
		
		List<LectureWeekData> weekDTO = new ArrayList<LectureWeekData>();
		List<WeekInfo> weekList = us.getSubjectInfo().getLectureList();
		/*
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
		*/

		return weekDTO;
	}
}
