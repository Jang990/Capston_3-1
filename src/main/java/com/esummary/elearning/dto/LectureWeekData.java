package com.esummary.elearning.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureWeekData {
	private String lectureWeekId; 
	private String title;
	private Date startDate;
	private Date endDate;
	private List<LectureData> lectures; //주차정보를 부르고. 주차정보를 클릭하면 정보를 가져오는 식으로 하는게 좋겠다.
}
