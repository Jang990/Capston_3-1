package com.esummary.crawling.service;

import java.util.List;

import com.esummary.crawling.dto.InhatcSubjectCardDTO;
import com.esummary.crawling.dto.InhatcUserDTO;
import com.esummary.crawling.dto.tofront.LectureWeekData;
import com.esummary.crawling.dto.tofront.NoticeData;
import com.esummary.crawling.dto.tofront.TaskData;

/**
 * 크롤링하고 db에 저장. 이 두개를 나누기엔 너무 멀리옴
 * @author User
 *
 */
public interface CrawlingService {
	/**
	 * 사용자정보를 받아서 로그인페이지 정보(수강하는 과목들 정보, 학번, 이름) 크롤링
	 * @param userDTO
	 * @return 크롤링한 과목정보 반환
	 */
	List<InhatcSubjectCardDTO> crawlLoginPage(InhatcUserDTO userDTO);
	
	/**
	 * subjectId에 관한 과제정보를 크롤링해서 DB에 저장
	 * @param userDTO
	 * @param subjectId
	 * @return
	 */
	List<TaskData> crawlTask(InhatcUserDTO userDTO, String subjectId);

	/**
	 * subjectId에 관한 공지정보를 크롤링해서 DB에 저장
	 * @param userDto
	 * @param subjectId
	 * @return
	 */
	List<NoticeData> crawlNotice(InhatcUserDTO userDto, String subjectId);
	
	/**
	 * subjectId에 관한 수업(수업 주차 + 강의 차시)정보를 크롤링해서 DB에 저장
	 * @param userDto
	 * @param subjectId
	 * @return
	 */
	List<LectureWeekData> crawlLecture(InhatcUserDTO userDto, String subjectId);
}
