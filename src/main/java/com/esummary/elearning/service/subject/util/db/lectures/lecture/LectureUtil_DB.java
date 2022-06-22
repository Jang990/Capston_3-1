package com.esummary.elearning.service.subject.util.db.lectures.lecture;

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
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.subject.SubjectLectureRepository;
import com.esummary.elearning.repository.subject.SubjectLectureWeekRepository;
import com.esummary.elearning.repository.subject.SubjectTaskRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.service.subject.ELearningServiceImpl;
import com.esummary.elearning.service.subject.util.crawling.SubjectUtil_Inhatc;

@Component
public class LectureUtil_DB implements DBLectureUtil {
	
	@Autowired
	private SubjectLectureRepository subjectLectureRepository;
	@Autowired
	private UserLectureRepository userLectureRepository;

	@Override
	public List<SubjectLecture> getLectureList(SubjectLectureWeekInfo subjectLectureWeekInfo) {
		List<SubjectLecture> lectureList = new ArrayList<SubjectLecture>();
		lectureList = subjectLectureRepository.findBySubjectLectureWeekInfo(subjectLectureWeekInfo);
		
		return lectureList;
	}
	
	@Override
	public UserLecture getUserLecture(int usId, SubjectLecture lecture) {
		UserLecture ul = userLectureRepository.findByUserSubject_usIdAndSubjectLecture_lectureId(usId, lecture.getLectureId());
		return ul;
	}
	
}
