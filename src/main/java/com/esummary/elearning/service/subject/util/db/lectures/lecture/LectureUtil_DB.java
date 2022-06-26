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

	@Override
	public boolean saveService(SubjectLecture lecture) {
		if(validateDuplicate(lecture))
			return false;
		
		subjectLectureRepository.save(lecture);
		return true;
	}

	@Override
	public boolean saveService(List<SubjectLecture> lectures) {
		List<SubjectLecture> savedLectureWeeks = new ArrayList<SubjectLecture>();
		
		for (SubjectLecture lecture : lectures) {
			if(validateDuplicate(lecture)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedLectureWeeks.add(lecture);
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		subjectLectureRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(SubjectLecture lecture) {
		SubjectLecture lectureCheck = subjectLectureRepository.
				findBySubjectLectureWeekInfo_LectureWeekIdAndIdx(lecture.getLectureWeekId(), lecture.getIdx());
		
		if(lectureCheck == null) return false;
		if(checkEntityValue(lecture, lectureCheck)) return false; 
		
		return true; //중복 맞음
	}
	
	//두 UserLecture Entity의 실제 값을 비교.
	private boolean checkEntityValue(SubjectLecture lecture1, SubjectLecture lecture2) {
		if(lecture1.getFullTime().equals(lecture2.getFullTime()) &&
				lecture1.getTitle().equals(lecture2.getTitle())) {
			if(!lecture1.getType().equals("온라인")) return true; // 실제 값이 같음.
			if(lecture1.getLectureVideoId().equals(lecture2.getLectureVideoId())) return true;
			return false;
		}
		else
			return false;
	}
}
