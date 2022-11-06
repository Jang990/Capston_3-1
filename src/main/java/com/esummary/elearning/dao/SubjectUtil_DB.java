package com.esummary.elearning.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.entity.chat.ChatRoom;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.subject.WeekInfo;
import com.esummary.repository.chat.ChatRoomRepository;
import com.esummary.repository.subject.SubjectInfoRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubjectUtil_DB implements DBSubjectUtil {
	
	private final SubjectInfoRepository subjectRepository;
	
	@Override
	public boolean saveService(SubjectInfo subject) {
		if(validateDuplicate(subject))
			return false;
		
		subjectRepository.save(subject);
		return true;
	}
	
	@Override
	public boolean saveService(List<SubjectInfo> subjects) {
		List<SubjectInfo> savedSubjects = new ArrayList<SubjectInfo>();
		
		for (SubjectInfo subject : subjects) {
			if(validateDuplicate(subject)) // 중복 확인, 중복일시 예외발생
				continue;
			else {
				//채팅방도 추가
				subject.setChatRoom(ChatRoom.createRoom(subject));
				savedSubjects.add(subject);
			}
		}
		
		if(savedSubjects.size() == 0) return false;
		
		subjectRepository.saveAll(savedSubjects);
		return true;
	}

	@Override
	public boolean validateDuplicate(SubjectInfo subject) {
		Optional<SubjectInfo> subjectCheck = subjectRepository.findSingleSubject(subject.getSubjectId());
		
		if(subjectCheck.isEmpty()) return false;
		else {
			// 중복시 DB에 저장된 채팅방 연결
			subject.setChatRoom(subjectCheck.get().getChatRoom());
			return true; //중복 맞음
		}
	}

	@Override
	public SubjectInfo getSubjectAllDetails(String subjectId) {
		SubjectInfo subjectInAllData = subjectRepository.findBySubjectId(subjectId).get();
		List<WeekInfo> lectureWeeks = subjectInAllData.getLectureList();
		for (WeekInfo subjectLectureWeekInfo : lectureWeeks) {
			subjectLectureWeekInfo.getLectures();
		}
		subjectInAllData.getNoticeList();
		subjectInAllData.getTaskList();
		return subjectInAllData;
	}
	
	
}
