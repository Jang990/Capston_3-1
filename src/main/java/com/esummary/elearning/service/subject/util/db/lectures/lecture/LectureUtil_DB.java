package com.esummary.elearning.service.subject.util.db.lectures.lecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esummary.elearning.entity.subject.SubjectLecture;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;
import com.esummary.elearning.entity.user.UserLecture;
import com.esummary.elearning.repository.subject.SubjectLectureRepository;
import com.esummary.elearning.repository.user.UserLectureRepository;
import com.esummary.elearning.service.subject.util.db.user.UserLectureUtil_DB;

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
	public List<SubjectLecture> getLectureList(SubjectLectureWeekInfo subjectLectureWeekInfo) {
		List<SubjectLecture> lectureList = new ArrayList<SubjectLecture>();
		lectureList = subjectLectureRepository.findBySubjectLectureWeekInfo(subjectLectureWeekInfo);
		
		return lectureList;
	}
	
	@Override
	public Optional<UserLecture> getUserLecture(int usId, SubjectLecture lecture) {
		Optional<UserLecture> ul = userLectureUtil_DB.getUserLecture(usId, lecture.getLectureId());
		return ul;
	}

	@Override
	public boolean saveService(SubjectLecture lecture) {
		if(validateDuplicate(lecture))
			return false;
		
		lecture.setLectureId(seq_Leture++); // MySQL로 바꾸고 삭제할 코드
		subjectLectureRepository.save(lecture);
		return true;
	}

	@Override
	public boolean saveService(List<SubjectLecture> lectures) {
		List<SubjectLecture> savedLectureWeeks = new ArrayList<SubjectLecture>();
		
		for (SubjectLecture lecture : lectures) {
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
	public boolean validateDuplicate(SubjectLecture lecture) {
		Optional<SubjectLecture> lectureCheck = getLecture(lecture.getLectureWeekId(), lecture.getIdx());
		
		if(lectureCheck.isEmpty()) return false; //중복 아님
		if(!equalEntityValue(lecture, lectureCheck.get())) return false; //중복 아님 
		
		return true; //중복 맞음
	}
	
	//두 UserLecture Entity의 실제 값을 비교.
	private boolean equalEntityValue(SubjectLecture lecture1, SubjectLecture lecture2) {
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
	public Optional<SubjectLecture> getLecture(String lectureWeekId, String idx) {
		return subjectLectureRepository.
				findBySubjectLectureWeekInfo_LectureWeekIdAndIdx(lectureWeekId, idx);
	}
}
