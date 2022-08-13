package com.esummary.elearning.service.dao.lectures.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.repository.subject.SubjectLectureRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.service.dao.user.UserLectureUtil_DB;

@Component
public class LectureUtil_DB implements DBLectureUtil {
	
	private static Long seq_Leture = 1L; //임시로 Lecture를 DB에 등록하기위해 만들어놓음 시퀀스사용할 것
	
	@Autowired
	private SubjectLectureRepository subjectLectureRepository;
	@Autowired
	private UserLectureRepository userLectureRepository;
	@Autowired
	private UserLectureUtil_DB userLectureUtil_DB; 

	@Override
	public List<LectureInfo> getLectureList(WeekInfo subjectLectureWeekInfo) {
		List<LectureInfo> lectureList = new ArrayList<LectureInfo>();
		lectureList = subjectLectureRepository.findBySubjectLectureWeekInfo(subjectLectureWeekInfo);
		
		return lectureList;
	}
	
	@Override
	public Optional<UserLecture> getUserLecture(int usId, LectureInfo lecture) {
		Optional<UserLecture> ul = userLectureUtil_DB.getUserLecture(usId, lecture.getLectureId());
		return ul;
	}

	@Override
	public boolean saveService(LectureInfo lecture) {
		if(validateDuplicate(lecture))
			return false;
		
		lecture.setLectureId(seq_Leture++); // MySQL로 바꾸고 삭제할 코드
		subjectLectureRepository.save(lecture);
		return true;
	}

	@Override
	public boolean saveService(List<LectureInfo> lectures) {
		List<LectureInfo> savedLectureWeeks = new ArrayList<LectureInfo>();
		
		for (LectureInfo lecture : lectures) {
			if(validateDuplicate(lecture)) // 중복 확인, 중복일시 예외발생
				continue;
			else {
				lecture.setLectureId(seq_Leture++); // MySQL로 바꾸고 삭제할 코드
				savedLectureWeeks.add(lecture);
			}
		}
		
		if(savedLectureWeeks.size() == 0) return false;
		
		subjectLectureRepository.saveAll(savedLectureWeeks);
		return true;
	}

	@Override
	public boolean validateDuplicate(LectureInfo lecture) {
		Optional<LectureInfo> lectureCheck = getLecture(lecture.getLectureWeekId(), lecture.getIdx());
		
		if(lectureCheck.isEmpty()) return false; //중복 아님
		if(!equalEntityValue(lecture, lectureCheck.get())) return false; //중복 아님 
		
		return true; //중복 맞음
	}
	
	//두 UserLecture Entity의 실제 값을 비교.
	private boolean equalEntityValue(LectureInfo lecture1, LectureInfo lecture2) {
		if(!lecture1.getTitle().equals(lecture2.getTitle())) return true;
		if(!(lecture1.getLectureVideoId() != null)) {
			if(!lecture1.getLectureVideoId().equals(lecture2.getLectureVideoId())) return true;
		}
		if(!lecture1.getLearningTime().equals(lecture2.getLearningTime())) return true;
		
		return false;
		/*
		if(lecture1.getFullTime().equals(lecture2.getFullTime()) &&
				lecture1.getTitle().equals(lecture2.getTitle())) {
			if(!lecture1.getType().equals("온라인")) return true; // 실제 값이 같음.
			if(lecture1.getLectureVideoId() == null) return true;
			return false;
		}
		else
			return false;
		*/
	}

	@Override
	public Optional<LectureInfo> getLecture(String lectureWeekId, String idx) {
		return subjectLectureRepository.
				findBySubjectLectureWeekInfo_LectureWeekIdAndIdx(lectureWeekId, idx);
	}
}
