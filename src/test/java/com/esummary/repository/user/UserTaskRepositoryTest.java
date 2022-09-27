package com.esummary.repository.user;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserSubject;
import com.esummary.repository.UserSubjectRepository;

@SpringBootTest
class UserTaskRepositoryTest {

	@Autowired
	private UserSubjectRepository repository;
	
	@Test
	void test() {
//		repository.findByUserInfo_StudentNumber("201845096");
		System.out.println(repository.existsByUserInfo_StudentNumberAndSubjectInfo_SubjectId("201845096", "202224001LLA103"));
	}
	
	@Test
	void test2() {
		repository.findWithUserTaskBySubjectInfo_SubjectIdAndUserInfo_StudentNumber("202224043DMP636", "201845096");
	}
	
	@Transactional
	@Test
	void test3() {
		String studentId = "201845096";
		String subjectId = "202224001LLA103";
		System.out.println("==========테스트 시작1");
		UserSubject us = repository
			.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentId)
			.orElseThrow(() -> new IllegalArgumentException("해당 과목이 존재하지 않음. 학번: "+studentId+", 과목ID: "+subjectId));
		
		System.out.println("==========테스트 시작2");
		SubjectInfo sb = us.getSubjectInfo();
		System.out.println(sb.getSubjectName());
		
		
		
		System.out.println("==========테스트 시작3");
		//쿼리 안보냄
		List<WeekInfo> weekList = us.getSubjectInfo().getLectureList();
		//쿼리를 보면 list를 전부 가져와서 1차캐시에 저장하고 하나씩 빼주는 듯
		WeekInfo week = us.getSubjectInfo().getLectureList().get(0);
//		WeekInfo week2 = us.getSubjectInfo().getLectureList().get(1);
	}

}
