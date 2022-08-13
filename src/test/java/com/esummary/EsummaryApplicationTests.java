package com.esummary;

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

import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.service.dao.SubjectUtil_DB;
import com.esummary.elearning.service.login.LoginService;
import com.esummary.elearning.service.vue.VueService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


@SpringBootTest
class EsummaryApplicationTests {
	
	@Autowired
	private VueService vueService;
	@Autowired
	private LoginService loginService;
	
	@Test
	public void CrawlWeekAndLecture() {
		String studentNumber = "201845096";
		String password = "...";
		String subjectId = "202211141LLA104";
		Map<String, String> loginCookies = loginService.getLoginCookies(studentNumber, password);
		UserData user = new UserData(studentNumber, password, loginCookies);
		vueService.crawlLecture(user, subjectId);
	}
	
//	@Test
	public void hikariConfig() {
		HikariConfig h = new HikariConfig();
		h.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		h.setJdbcUrl("jdbc:log4jdbc:mysql://localhost:4306/InhaTcES");
		h.setUsername("root");
		h.setPassword("1234");
		DataSource dataSource = new HikariDataSource(h);
		System.out.println("===============>" + dataSource.toString());
	}
	
	
}
