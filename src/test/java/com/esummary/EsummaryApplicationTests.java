package com.esummary;

import java.security.Key;
import java.util.Map; 

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.esummary.auth.dto.LoginDTO;
import com.esummary.auth.service.ElearningLoginService;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.exservice.crawling.SubjectCrawlingService;
import com.esummary.elearning.exservice.crawling.user.UserCrawlingUtil;
import com.esummary.elearning.exservice.login.LoginService;
import com.esummary.elearning.exservice.subject.SubjectDBService;
import com.esummary.elearning.exservice.vue.VueService;
import com.esummary.elearning.repository.subject.SubjectInfoRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@SpringBootTest
class EsummaryApplicationTests {
	
	@Autowired
	private SubjectDBService subjectDBService;
	@Autowired
	private VueService vueService;
	@Autowired
	private ElearningLoginService elearningLoginService;
	@Autowired
	private SubjectCrawlingService subjectCrawlingService;
	
	@Autowired
	private SubjectInfoRepository subjectInfoRepository;
	@Autowired
	private UserCrawlingUtil userCrawlingUtil;
	
	
//	@Test
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
//		SubjectInfo si = subjectInfoRepository.findBySubjectId("202214043C4846");
//		System.out.println("=====>여기: "+si.getLectureList().get(0).getTitle());
//		System.out.println("여기까지");
	}
	
	
	@Test
	public void CrawlWeekAndLecture() {
		String studentNumber = "201845096";
		String password = "..."; //비밀번호로 변경할 것
		String subjectId = "202211141LLA104";
		LoginDTO loginCheck = new LoginDTO();
		loginCheck.setStudentNumber(studentNumber);
		loginCheck.setPassword(password);
		
		Map<String, String> loginCookies = elearningLoginService.getLoginCookies(loginCheck);
		System.out.println("=========>"+loginCookies);
		subjectCrawlingService.crawlBasicSubjectInfo(loginCookies);
		
	}
	
	
	@Test
	public void testToken() {
		String secret = "c2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQtc2lsdmVybmluZS10ZWNoLXNwcmluZy1ib290LWp3dC10dXRvcmlhbC1zZWNyZXQK";
    	Key key;
    	byte[] bytesKey = Decoders.BASE64.decode(secret);
		key = Keys.hmacShaKeyFor(bytesKey);
    	
    	Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMDE4NDUwOTYiLCJhdXRoIjoiVVNFUiIsIkVJZCI6eyJXTU9OSUQiOiJnbWhEMEJZd0xsRiIsIkpTRVNTSU9OSUQiOiJhYWFFTU45R3EwWmNuX3JNUWdlbHlZQXlQR19CSXAwLU4zSGZQV0N5b1o4Y09lMDlTS1dfdC1fU2RydngifSwiZXhwIjoxNjY0MTU4NjA2fQ.7c0Fv_Ypu9XGa1zjTYuq1PmncdALOYfzYDCCprsx2gqVDiFHSu1JqpHv8DEIP2592hPRdUotL7VmuoR3qYqkUQ")
				.getBody();
    	
//    	{WMONID=gmhD0BYwLlF, JSESSIONID=aaaEMN9Gq0Zcn_rMQgelyYAyPG_BIp0-N3HfPWCyoZ8cOe09SKW_t-_Sdrvx} 로 출력된다.
    	Map<String, String> log = (Map<String, String>) claims.get("EId");
    	System.out.println(log);
	}
}
