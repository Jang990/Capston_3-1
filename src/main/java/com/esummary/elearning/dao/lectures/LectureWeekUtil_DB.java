package com.esummary.elearning.dao.lectures;

import java.text.ParseException; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.dao.lectures.lecture.DBLectureUtil;
import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.subject.TaskInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.entity.user.UserSubject;
import com.esummary.elearning.entity.user.UserTask;
import com.esummary.elearning.repository.UserSubjectRepository;
import com.esummary.elearning.repository.subject.LectureInfoRepository;
import com.esummary.elearning.repository.subject.WeekInfoRepository;
import com.esummary.elearning.repository.subject.TaskInfoRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.service.crawling.ELearningURL;
import com.esummary.elearning.service.crawling.SubjectCrawlingService_Inhatc;

@Component
public class LectureWeekUtil_DB implements DBLectureWeekUtil {
	@Autowired
	private WeekInfoRepository WeekInfoRepository;
	
	@Override
	public boolean saveService(WeekInfo lectureWeek) {
		if(validateDuplicate(lectureWeek))
			return false;
		
		WeekInfoRepository.save(lectureWeek);
		return true;
	}

	@Override
	public boolean saveService(List<WeekInfo> lectureWeeks) {
		List<WeekInfo> savedLectureWeeks = new ArrayList<WeekInfo>();
		
		for (WeekInfo lectureWeek : lectureWeeks) {
			if(validateDuplicate(lectureWeek)) // 중복 확인, 중복일시 예외발생
				continue;
			else savedLectureWeeks.add(lectureWeek);
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		WeekInfoRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(WeekInfo lectureWeek) {
		Optional<WeekInfo> WeekCheck = WeekInfoRepository.
				findByWeekIdAndSubjectInfo_subjectId(lectureWeek.getWeekId(), lectureWeek.getSubjectId());
		
		if(WeekCheck.isEmpty()) return false;
		else return true; //중복 맞음
	}

}
