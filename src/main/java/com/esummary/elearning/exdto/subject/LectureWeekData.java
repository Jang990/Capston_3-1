package com.esummary.elearning.exdto.subject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.esummary.elearning.entity.subject.LectureInfo;
import com.esummary.elearning.entity.subject.WeekInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureWeekData {
	private String lectureWeekId; 
	private String title;
	private String startDate;
	private String endDate;
	private List<LectureData> lectures; //주차정보를 부르고. 주차정보를 클릭하면 정보를 가져오는 식으로 하는게 좋겠다.
	
	private int cntCompleted;
	private int cntIncompleted; 
	private int studyingState; 
	
	public LectureWeekData(WeekInfo lectureWeekInfo) {
		int cntCompleted = 0;
		int cntIncompleted = 0;
		List<LectureInfo> lectureDetail = lectureWeekInfo.getLectures();
		List<LectureData> lectureDetailDTO = new ArrayList<LectureData>();
		String startDate = makeDateString(lectureWeekInfo.getStartDate());
		String endDate = makeDateString(lectureWeekInfo.getEndDate());
		
		for (LectureInfo detail : lectureDetail) {
			
			LectureData lecture = new LectureData(detail);
			if(isCompletedLecture(lecture)) cntCompleted++;
			else cntIncompleted++;
			
			lectureDetailDTO.add(lecture);
		}
		
		int studyingState = 0;
		if(cntCompleted != 0 || cntIncompleted != 0) {
			studyingState = (int)((float)cntCompleted / (cntCompleted + cntIncompleted) * 100); 
		}
		
		this.lectureWeekId = lectureWeekInfo.getWeekId();
		this.title = lectureWeekInfo.getTitle();
		this.startDate = startDate;
		this.endDate = endDate;
		this.lectures = lectureDetailDTO;
		this.cntCompleted = cntCompleted;
		this.cntIncompleted = cntIncompleted;
		this.studyingState = studyingState;
	}
	
	
	
	private String makeDateString(Date startDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		return calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1) + "/" + calendar.get(Calendar.DATE)
		+ " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE);
	}
	
	private boolean isCompletedLecture(LectureData lecture) {
		if(lecture.getFullTime() == null) return true; //수업 시간이 없다면 해야할 일이 아니다. 화상강의는 알아해라
		System.out.println("오류제어 ======== " + lecture);
		int fullTime = getMinuteNumber(lecture.getFullTime());
		int studyTime = getMinuteNumber(lecture.getLearningTime());
		if(fullTime <= studyTime && lecture.getStatus().equals("1")) return true;
		return false;
	} 
	
	private int getMinuteNumber(String minuteString) {
		if(!minuteString.contains("분")) return 0;
		return Integer.parseInt(minuteString.substring(0, minuteString.indexOf("분")));
	}

}
