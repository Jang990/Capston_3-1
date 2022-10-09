package com.esummary.repository.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.entity.subject.LectureInfo;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.entity.user.UserSubject;
import com.esummary.repository.UserSubjectRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

@SpringBootTest
class UserTaskRepositoryTest {

	@Autowired
	private UserSubjectRepository repository;
	
	@Test
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
	@Test
	@DisplayName("lecture 정보 가져오기 테스트")
	void test() {
		//	weekInfo테이블에서 subjectId에 해당하는 정보를 뽑아내고 그 정보에서 lectureInfo테이블 정보를 가져오기
		String subjectId = "202224001LLA103";
		String studentId = "201845096";
		
		System.out.println(repository.existsByUserInfo_StudentNumberAndSubjectInfo_SubjectId(studentId, subjectId));
		
	}

}
