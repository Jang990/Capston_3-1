package com.esummary.elearning.service.subject.util.db.lectures;

import java.text.ParseException; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.subject.SubjectTaskInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.SubjectLectureRepository;
import com.esummary.elearning.repository.subject.SubjectLectureWeekRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil_Inhatc;
import com.esummary.elearning.service.subject.util.db.lectures.lecture.DBLectureUtil;

@Component
public class LectureWeekUtil_DB implements DBLectureWeekUtil {
	
	@Autowired
	private DBLectureUtil lectureUtil;
	
	@Autowired
	private SubjectLectureWeekRepository subjectLectureWeekRepository;

	@Autowired
	private UserSubjectRepository userSubjectRepository;
	
	@Override
	public List<SubjectLectureWeekInfo> getSubjectLectureInfo(SubjectInfo subjectInfo) {
		List<SubjectLectureWeekInfo> weekList = new ArrayList<SubjectLectureWeekInfo>();
		weekList = subjectLectureWeekRepository.findBySubjectInfo(subjectInfo);
		for (SubjectLectureWeekInfo subjectLectureWeekInfo : weekList) {
			List<SubjectLecture> lectures = lectureUtil.getLectureList(subjectLectureWeekInfo);
			subjectLectureWeekInfo.setLectures(lectures);
		}
		return weekList;
	}
	
	@Override
	public List<SubjectLectureWeekInfo> getSubjectLectureInfo(String subjectId) {
		List<SubjectLectureWeekInfo> weekList = new ArrayList<SubjectLectureWeekInfo>();
		weekList = subjectLectureWeekRepository.findBySubjectInfo_subjectId(subjectId);
		for (SubjectLectureWeekInfo subjectLectureWeekInfo : weekList) {
			List<SubjectLecture> lectures = lectureUtil.getLectureList(subjectLectureWeekInfo);
			subjectLectureWeekInfo.setLectures(lectures);
		}
		return weekList;
	}

	@Override
	public List<UserLecture> getUserlecture(UserSubject us, List<SubjectLectureWeekInfo> weekList) {		
		if(us == null) return null;
		List<UserLecture> userLectureList = new ArrayList<>();
		
		for (SubjectLectureWeekInfo subjectLectureWeekInfo : weekList) {
			List<SubjectLecture> lectureList = subjectLectureWeekInfo.getLectures();
			for (SubjectLecture lecture : lectureList) {
				UserLecture ul = lectureUtil.getUserLecture(us.getUsId(), lecture);
				userLectureList.add(ul);
			}
		}
		
		return userLectureList;
	}

}
