package com.esummary;

import java.sql.Timestamp;
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
import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.vue.VueService;
import com.esummary.entity.TestModel;
import com.esummary.repository.TestRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@SpringBootTest
class EsummaryApplicationTests {
	
	@Autowired
	private VueService vueService;
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private TestRepository testRepository;
	
	@Test
	public void checkTime() {
		TestModel test = new TestModel();
		try {
			test.setName("Jang");
			testRepository.save(test);
			
			Thread.sleep(3*1000);
			testRepository.save(test);
						
			Thread.sleep(3*1000);
			test.setName("Kim");
			testRepository.save(test);
			
			Thread.sleep(3*1000);
			Timestamp timestamp = new Timestamp(0);
			test.setCrawlingTime(timestamp);
			test.setCreateTime(timestamp);
			testRepository.save(test);
			
			Thread.sleep(3*1000);
//			test.setCreateTime(timestamp);
//			test.setTestId(100L);
//			testRepository.save(test);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void CrawlWeekAndLecture() {
		String studentNumber = "201845096";
		String password = "...";
		String subjectId = "202211141LLA104";
		Map<String, String> loginCookies = loginService.getLoginCookies(studentNumber, password);
		UserData user = new UserData(studentNumber, password, loginCookies);
		vueService.crawlLecture(user, subjectId);
	}
	
}
