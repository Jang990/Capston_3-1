package com.esummary.elearning.excontroller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.elearning.exdto.subject.LectureWeekData;
import com.esummary.elearning.exdto.subject.NoticeData;
import com.esummary.elearning.exdto.subject.SubjectCountData;
import com.esummary.elearning.exdto.subject.SubjectDetailDataWithCnt_DTO;
import com.esummary.elearning.exdto.subject.TaskData;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.elearning.exservice.subject.SubjectDBService;

@RestController
public class exSubjectController {
	
	@Autowired
	private SubjectDBService subjectDBService;
	
	//DB에서 가져오기
	@RequestMapping("/getSubjectInDB")
	public SubjectDetailDataWithCnt_DTO subjectSearch(HttpServletRequest request, @RequestParam String subjectId) {
		//해당 학생이 US테이블에 subjectId가 있는지 검사해야한다. 세션데이터와 비교하면 될 것 같다
		List<LectureWeekData> lectureDTO = this.lectureSearch(request, subjectId);
		List<NoticeData> noticeDTO = this.noticeSearch(request, subjectId);
		List<TaskData> taskDTO = this.taskSearch(request, subjectId);
		SubjectCountData cntDTO = new SubjectCountData(lectureDTO, taskDTO);
		
		SubjectDetailDataWithCnt_DTO subjectDTO = new SubjectDetailDataWithCnt_DTO(lectureDTO, taskDTO, noticeDTO, cntDTO);
		return subjectDTO;
	}
	//강의 주차 검색
	@RequestMapping("/lectureDB")
	public List<LectureWeekData> lectureSearch(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<LectureWeekData> lectureWeekList = subjectDBService.getLectureData(user, subjectId); 
		return lectureWeekList;
	}
	
	//과제 검색 
	@RequestMapping("/taskDB")
	public List<TaskData> taskSearch(HttpServletRequest request, @RequestParam String subjectId) {
		UserData user = (UserData)request.getSession().getAttribute("userData");
		List<TaskData> taskList = subjectDBService.getTaskData(user, subjectId);
		return taskList;
	}
	
	//공지 검색
	@RequestMapping("/noticeDB")    
	public List<NoticeData> noticeSearch(HttpServletRequest request, @RequestParam String subjectId) {
		List<NoticeData> notices = subjectDBService.getNoticeData(subjectId);
		if(notices == null) return null;
		
		return notices;
	}
	
}
