package com.esummary.elearning.service.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil;

@Service
public class TestServiceImpl implements TestService {
	
	@Autowired
	@Qualifier("DB")
	private SubjectUtil subjectUtil_DB;
	
	@Autowired
	private UserRepository userRepository;
	
	
	//DB 조회 테스트 진행중 - 22/05/06 완료
	//이제 화면구성 들어가야 할듯.
	@Override
	public List<SubjectInfo> test(UserInfo user) {
		List<SubjectInfo> subjectList = subjectUtil_DB.getSubjectList(user); //전체 조회를 한번에 하면 1~2초정도 걸림. 나눠서 조회해야 할듯
		user.setSubjectList(subjectList);
		return user.getSubjectList();
	}
	
	public UserInfo getUserInfo(String studentNumber) {
		UserInfo user = userRepository.findByStudentNumber(studentNumber);
		return user;
	}

}
