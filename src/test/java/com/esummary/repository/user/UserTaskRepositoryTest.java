package com.esummary.repository.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

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
	
	@Transactional
	@Test
	void testQueryDSL1() {
		System.out.println("=========테스트 시작1");
		
		String studentId = "201845096";
		String subjectId = "202224001LLA103";
//		String subjectId = "202224043DMP636";
		System.out.println("====>시작");
		List<UserSubject> usList = repository.findLectureInfo(studentId, subjectId);
		System.out.println("====>끝");
		
//		System.out.println(usList.get(0).getSubjectInfo().getSubjectOwnerName());
		/*
		for (UserSubject userSubject : usList) {
			System.out.println("======"+userSubject.getSubjectInfo().getSubjectName());
			for (WeekInfo weekInfo : userSubject.getSubjectInfo().getLectureList()) {
				for (LectureInfo lecture : weekInfo.getLectures()) {
					System.out.println(lecture.getTitle());
				}
			}
		}
		*/
	}
	
//	@Transactional
	@Test
	void testQueryDSL2() {
		String subjectId = "202224001LLA103";
		String studentId = "201845096";
		
		/*
		 * LectureWeekData - 이 DTO로 변환하고 SubjectDetailDataWithCnt_DTO에 setter로 붙혀서 클라이언트로 보내면 되겠다.
		 * 하지만 LectureWeekData에서 UserLecture 테이블을 자동적으로 넣는 코드가 있다. 
		 * 아무래도 크롤링하고 바로 넣을때 쓰는 DTO이기 때문에 문제가 있다. - 근본적으로 엔티티의 @Transient 문제이다.
		 * QueryDSL로 조인해서 UserLecture의 내용을 가져오고 LectureWeekData에 넣는 부분이 추가되어야 한다. 
		 */
		List<WeekInfo> weList1 = repository.test(studentId, subjectId);
//		List<LectureWeekData> lecDtoList = new ArrayList<>();
//		for (int i = 0; i < weList1.size(); i++) {
//			lecDtoList.add(new LectureWeekData(weList1.get(i)));
//		}
		
	}

}
