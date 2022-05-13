package com.esummary.elearning.service.subject.util.db;

import java.util.ArrayList; 
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
public class SubjectUtil_DB implements SubjectUtil{
	
	@Autowired
	private DBTaskUtil taskUtil;
	@Autowired
	private DBNoticeUtil noticeUtil;
	@Autowired
	private DBLectureWeekUtil lectureUtil;
	
	@Autowired
	private UserSubjectRepository userSubjectRepository;
	
	@Override
	public List<SubjectInfo> getSubjectList(UserInfo user) {
		List<SubjectInfo> subjectList = this.getOwnSubject(user);
		user.setSubjectList(subjectList);
		getSubjectDetail(user);
		return subjectList;
	}
	
	private List<SubjectInfo> getOwnSubject(UserInfo user) {
		List<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
		
		List<UserSubject> us = null;
		us = userSubjectRepository.findByUserInfo(user);
		user.setUserSubjects(us);
		for (UserSubject userSubject : us) {
			SubjectInfo subjectInfo = userSubject.getSubjectInfo();
			subjectList.add(subjectInfo);
			
//			System.out.println("ㅇㅁㅂ:" + userSubject.getSubjectInfo().getSubjectName()); //이때 쿼리를 보냄 LAZY
//			System.out.println("ㅇㅁㅂ:" + userSubject.getSubjectInfo().getNoticeList());//이런것은 불가능
		}
		
		/*내 생각에는 Lazy로 되어있는데 sysout(us)를 하면서 없는 것을 가져와서 그런듯 UserSubject를 EAGER로 바꿈 - 안된다.
		위에 SYSOUT부분같은부분에 getNoticeList이런것들이 아예 안들어가 있어서 그런듯*/
//		System.out.println("단테:" + us); // 오류
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
			
			//과제 불러오기
			taskList = taskUtil.getSubjectTaskInfo(subjectInfo);
			subjectInfo.setTaskList(taskList);
			userTaskList = taskUtil.getUserTask(taskList);
			userSubject.setUserTask(userTaskList);
			
			//공지사항 불러오기
			noticeList = noticeUtil.getSubjectNoticeInfo(subjectInfo);
			subjectInfo.setNoticeList(noticeList);
			
			//강의 불러오기
			lectureList = lectureUtil.getSubjectLectureInfo(subjectInfo);
			subjectInfo.setLectureList(lectureList);
			userLecture = lectureUtil.getUserlecture(lectureList);
			userSubject.setUserLecture(userLecture);
		}
		
	}
	
}
