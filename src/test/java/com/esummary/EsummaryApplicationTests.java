package com.esummary;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.esummary.elearning.dao.SubjectUtil_DB;
import com.esummary.elearning.dto.LectureWeekData;
import com.esummary.elearning.dto.user.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.repository.subject.SubjectInfoRepository;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.subject.SubjectDBService;
import com.esummary.elearning.service.vue.VueService;
import com.esummary.entity.TestModel;
import com.esummary.repository.TestRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@SpringBootTest
class EsummaryApplicationTests {
	
	@Autowired
	private SubjectDBService subjectDBService;
	@Autowired
	private VueService vueService;
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private TestRepository testRepository;
	
	@Autowired
	private SubjectInfoRepository subjectInfoRepository;
	
	@Test
	public void loadDB() {
		/*
		String num = "201845096";
		String id = "202214043C4846";
		UserData ud = new UserData(num, "장현우", new HashMap<String,String>());
		List<LectureWeekData> wd = subjectDBService.getLectureData(ud, id);
		System.out.println("==============>여기");
		System.out.println(wd);
		*/
		
		System.out.println("시작");
		SubjectInfo si = subjectInfoRepository.findBySubjectId("202214043C4846");
		System.out.println("=====>여기: "+si.getLectureList().get(0).getTitle());
		System.out.println("여기까지");
	}
	
	
//	@Test
	public void CrawlWeekAndLecture() {
		String studentNumber = "201845096";
		String password = "..."; //비밀번호로 변경할 것
		String subjectId = "202211141LLA104";
		Map<String, String> loginCookies = loginService.getLoginCookies(studentNumber, password);
		UserData user = new UserData(studentNumber, password, loginCookies);
		vueService.crawlLecture(user, subjectId);
	}
	
}
