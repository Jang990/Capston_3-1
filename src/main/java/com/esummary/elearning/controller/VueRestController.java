package com.esummary.elearning.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.dto.InitalPageData;
import com.esummary.elearning.dto.SubjectCardData;
import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.test.TestService;

@RestController
public class VueRestController {
	@Autowired
	private TestService testService;
	
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	@Autowired UserRepository userRepository;
	
	//Vue DB연동 테스트 
	@RequestMapping("/vueDBu")
	public UserInfo vueDB() {
		//간단하게 user를 찾아서 반환해 봤다.
		UserInfo user = testService.getUserInfo("201845096");
		user.setInitialCookies(null);
		user.setSubjectList(null);
		user.setUserSubjects(null);//하나라도 null이 있다면 프론트엔드에서 받을 수 없다. DTO를 사용할 것
		return user; 
	}
	
	//Vue DB연동 테스트 
	@RequestMapping("/vueDB")
	public InitalPageData vueDBSearch() {
		InitalPageData initPageData;
		String name = "장현우";
		String studentNumber = "201845096";
		
		UserInfo user = userRepository.findWithUserSubjectsByStudentNumber(studentNumber);
		List<SubjectCardData> cardList = new ArrayList<>();
		for (UserSubject userSubject : user.getUserSubjects()) {
			SubjectInfo subject = userSubject.getSubjectInfo();
			SubjectCardData card = new SubjectCardData(
					subject.getSubjectId(), subject.getSubjectName(), subject.getSubjectOwnerName());
			cardList.add(card);
		}
		
		if(cardList.size() <= 0) 
			cardList = null;
		initPageData = new InitalPageData(name, studentNumber, cardList);
		
		return initPageData;
	}
}
