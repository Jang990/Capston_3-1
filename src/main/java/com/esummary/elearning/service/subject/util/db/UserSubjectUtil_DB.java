package com.esummary.elearning.service.subject.util.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectNoticeInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil;
import com.esummary.elearning.service.subject.util.db.lectures.DBLectureWeekUtil;
import com.esummary.elearning.service.subject.util.db.notice.DBNoticeUtil;
import com.esummary.elearning.service.subject.util.db.task.DBTaskUtil;

@Component("DB")
public class UserSubjectUtil_DB implements DBUserSubjectUtil{
	
	private static int seqUserSubjectNum = 0; // 시퀸스 넘버. MySQL로 바꾸고 auto Increment로 바꿀것
	
	@Autowired
	private DBTaskUtil taskUtil;
	@Autowired
	private DBNoticeUtil noticeUtil;
	@Autowired
	private DBLectureWeekUtil lectureUtil;
	
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	
	@Override
	public boolean saveService(UserSubject userSubject) {
		if(validateDuplicate(userSubject)) // 중복 확인, 중복일시 예외발생
			return false;
		
		userSubject.setUsId(seqUserSubjectNum++); //이거 삭제하고 MySQL 로 바꾸기 OK?
		userSubjectRepository.save(userSubject); // DB 저장
		return true;
	}
	
	@Override
	public boolean saveService(List<UserSubject> userSubjects) {
		List<UserSubject> savedUserSubject = new ArrayList<UserSubject>();
		
		for (UserSubject userSubject : userSubjects) {
			if(validateDuplicate(userSubject)) // 중복 확인, 중복일시 예외발생
				continue;
			else {
				userSubject.setUsId(seqUserSubjectNum++); //이거 삭제하고 MySQL 로 바꾸기 OK?
				savedUserSubject.add(userSubject);
			}
		}
		
		if(savedUserSubject.size() == 0) return false;
		
		userSubjectRepository.saveAll(savedUserSubject); // DB 저장
		return true;
	}

	@Override
	public boolean validateDuplicate(UserSubject userSubject) {
		//auto Increment가 기본키이기 때문에 과목번호랑 학번이 중복되는건 저장하면 안된다. 
		UserSubject us = userSubjectRepository.findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(
				userSubject.getSubjectId(), userSubject.getStudentNumber());
		
		if(us == null) return false;
		else return true; //중복 맞음
	}

	@Override
	public UserSubject getStudentSubject(String subjectId, String studentNumber) {
		return userSubjectRepository
				.findWithSubjectInfoBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
	}
	
	@Override
	public List<UserSubject> getStudentsSubject(String studentNumber) {
		return userSubjectRepository.findByUserInfo_StudentNumber(studentNumber);
	}
	
	
	
	//?? getSubjectDetail부분떄문에 남겨놓음. 리팩터링 후 삭제할 것
	private List<SubjectInfo> getSubjectListIncludeDetail(UserInfo user) {
		List<SubjectInfo> subjectList = this.getOwnSubject(user.getStudentNumber());
		user.setSubjectList(subjectList);
		getSubjectDetail(user);
		return subjectList;
	}
	
	
	private List<SubjectInfo> getOwnSubject(/*UserInfo user 원래 이거 였음 */String studentNumber) {
		List<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
		
		List<UserSubject> us = getStudentsSubject(studentNumber);
//		user.setUserSubjects(us);
		for (UserSubject userSubject : us) {
			SubjectInfo subjectInfo = userSubject.getSubjectInfo();
			subjectList.add(subjectInfo);
		}
		
		return subjectList;
	}
	
	private void getSubjectDetail(UserInfo user) {
		List<SubjectTaskInfo> taskList = new ArrayList<>();
		List<UserTask> userTaskList = new ArrayList<>();

		List<SubjectNoticeInfo> noticeList = new ArrayList<>();
		
		List<SubjectLectureWeekInfo> lectureList = new ArrayList<>();
		List<UserLecture> userLecture = new ArrayList<>();
		
		List<UserSubject> userSubjects = user.getUserSubjects();
		for (UserSubject userSubject : userSubjects) {
			SubjectInfo subjectInfo = userSubject.getSubjectInfo();
			UserSubject us = getUserSubject(subjectInfo.getSubjectId(), user.getStudentNumber()); // 개인정보를 불러오기 위한 객체
			
			//과제 불러오기
				//사용자가 가지고 있는 과제에 대한 정보 불러오기
			taskList = taskUtil.getSubjectTaskInfo(subjectInfo);
			subjectInfo.setTaskList(taskList);
				//과제에 대한 개인정보 불러오기 (과제 제출여부 등등)
			userTaskList = taskUtil.getUserTask(taskList);
			userSubject.setUserTask(userTaskList);
			
			//공지사항 불러오기
			noticeList = noticeUtil.getSubjectNoticeInfo(subjectInfo);
			subjectInfo.setNoticeList(noticeList);
			
			//강의 불러오기
				//사용자가 가지고 있는 강의에 대한 정보 불러오기
			lectureList = lectureUtil.getSubjectLectureInfo(subjectInfo);
			subjectInfo.setLectureList(lectureList);
				//강의에 대한 개인정보 불러오기 (학생이 강의를 들은 시간, 학생이 강의를 완료했는지 등등)
			userLecture = lectureUtil.getUserlecture(us, lectureList);
			userSubject.setUserLecture(userLecture);
			
		}
		
	}
	
	private UserSubject getUserSubject(String subjectId, String studentNumber) {
		return userSubjectRepository.
			findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(subjectId, studentNumber);
	}
	
	private List<SubjectInfo> getNeedStoredSubjectData(List<UserSubject> dbUserSubject,
			List<SubjectInfo> crawlingSubjects) {   
		List<SubjectInfo> needStoredSubjects = new ArrayList<SubjectInfo>();
		
		String[] dbSubjectId = new String[dbUserSubject.size()];
		String[] crawlingSubjectId = new String[crawlingSubjects.size()];
		
		int i = 0;
		for (UserSubject userSubject : dbUserSubject) {
			dbSubjectId[i] = userSubject.getSubjectId();
			i++;
		}
		
		i = 0;
		for (SubjectInfo subjectInfo : crawlingSubjects) {
			crawlingSubjectId[i] = subjectInfo.getSubjectId();
			i++;
		}
		
		for (int j = 0; j < crawlingSubjectId.length; j++) {
			if(Arrays.asList(dbSubjectId).contains(crawlingSubjectId[j])) {
				continue;
			}
			else { 
				needStoredSubjects.add(crawlingSubjects.get(j));
			}
		}
		
		if(needStoredSubjects.size() == 0) {
			return null;
		}
		else {
			System.out.println(needStoredSubjects.toString());
			return needStoredSubjects;
		}
	}
	
}
