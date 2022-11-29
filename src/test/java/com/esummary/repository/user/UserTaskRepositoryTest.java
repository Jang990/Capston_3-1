package com.esummary.repository.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.crawling.dto.SubjectCountData;
import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.TaskData;
import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserSubject;
import com.esummary.entity.user.UserTask;
import com.esummary.repository.UserSubjectRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

//@SpringBootTest
class UserTaskRepositoryTest {

	@Autowired
	private UserSubjectRepository repository;
	
//	@Test
	@DisplayName("lecture 정보 가져오기 테스트")
	void testQueryDSL1() {
		//	weekInfo테이블에서 subjectId에 해당하는 정보를 뽑아내고 그 정보에서 lectureInfo테이블 정보를 가져오기
		String subjectId = "202224001LLA103";
		String studentId = "201845096";
		
		List<LectureWeekData> weList1 = repository.findUserLectureList(studentId, subjectId);
		for (LectureWeekData lectureWeekData : weList1) {
			lectureWeekData.calcCnt();
		}
		System.out.println("안녕");
		
	}
//	@Test
	@DisplayName("exists 테스트")
	void test() {
		//	weekInfo테이블에서 subjectId에 해당하는 정보를 뽑아내고 그 정보에서 lectureInfo테이블 정보를 가져오기
		String subjectId = "202224001LLA103";
		String studentId = "201845096";
		
		System.out.println(repository.existsByUserInfo_StudentNumberAndSubjectInfo_SubjectId(studentId, subjectId));
		
	}
	
//	@Test
	@DisplayName("이전 Task 정보 가져오기")
	void task() {
		String subjectId = "202224043QMP910";
		String studentId = "201845096";
		System.out.println("시작");
		UserSubject us = null;
//				repository.findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentId).get();
		
		// 문제점. 크롤링시에는 UserTask까지 Task엔티티에 담아버리나 db에서 가져올 때는 없음.
		// 해결방안querydsl로 dto에 그냥 담아버리자
		SubjectCountData sc = new SubjectCountData(null, null);
		List<TaskData> taskDTO = new ArrayList<TaskData>(); 
		List<UserTask> ut = us.getUserTask();
		for (UserTask userTask : ut) {
			System.out.println(userTask.getSubmitYN());
			TaskData td = TaskData.from(userTask.getTaskInfo());
			System.out.println(td);
			taskDTO.add(td);
		}
//		sc.countTask(taskDTO);
		System.out.println("끝");
	}
	
//	@Test
	@DisplayName("Task 정보 가져오기")
	void taskNow() {
		String subjectId = "202224043QMP910";
		String studentId = "201845096";
		System.out.println("시작");
		
		List<TaskData> taskDTO = repository.findUserTaskList(studentId, subjectId);
		for (TaskData taskData : taskDTO) {
			System.out.println(taskData);
		}
		System.out.println("끝");
	}

}
