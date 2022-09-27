package com.esummary.repository.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}
