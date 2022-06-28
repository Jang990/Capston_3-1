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
	private SubjectLectureWeekRepository subjectLectureWeekRepository;
	
	@Override
	public boolean saveService(SubjectLectureWeekInfo lectureWeek) {
		if(validateDuplicate(lectureWeek))
			return false;
		
		subjectLectureWeekRepository.save(lectureWeek);
		return true;
	}

	@Override
	public boolean saveService(List<SubjectLectureWeekInfo> lectureWeeks) {
		List<SubjectLectureWeekInfo> savedLectureWeeks = new ArrayList<SubjectLectureWeekInfo>();
		
		for (SubjectLectureWeekInfo lectureWeek : lectureWeeks) {
			if(validateDuplicate(lectureWeek)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedLectureWeeks.add(lectureWeek);
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		subjectLectureWeekRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(SubjectLectureWeekInfo lectureWeek) {
		SubjectLectureWeekInfo lectureWeekCheck = subjectLectureWeekRepository.
				findByLectureWeekIdAndSubjectInfo_subjectId(lectureWeek.getLectureWeekId(), lectureWeek.getSubjectId());
		
		if(lectureWeekCheck == null) return false;
		else return true; //중복 맞음
	}

}
