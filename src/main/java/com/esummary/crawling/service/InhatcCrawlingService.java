package com.esummary.crawling.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.esummary.auth.exception.DeniedElearningCookieException;
import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.InhatcUserDTO;
import com.esummary.crawling.dto.TaskData;
import com.esummary.crawling.dto.exInitalPageData;
import com.esummary.crawling.dto.exSubjectCardData;
import com.esummary.elearning.dao.DBSubjectUtil;
import com.esummary.elearning.dao.DBUserSubjectUtil;
import com.esummary.elearning.dao.task.DBTaskUtil;
import com.esummary.elearning.dao.user.DBUserTaskUtil;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.elearning.exservice.crawling.SubjectCrawlingService;
import com.esummary.elearning.exservice.crawling.task.TaskCrawlingService;

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
		Optional<UserSubject> userSubjectCheck = dbUserSubjectUtil.getStudentSubject(subjectId, user.getStudentId());
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

}
