package com.esummary;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.service.subject.util.db.SubjectUtil_DB;


@SpringBootTest
class EsummaryApplicationTests {
	
	@Autowired
	private SubjectUtil_DB testSubjectDB;
	
	@Test
	@Transactional
	void contextLoads() {
		SubjectInfo subject = testSubjectDB.getSubjectAllDetails("202214043C4846");
		System.out.println(subject);
	}

}
