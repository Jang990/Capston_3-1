package com.esummary.elearning.exdto.subject;

import com.esummary.elearning.entity.subject.LectureInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureData {
	//과목정보
	Long lectureId;
	String lectureVideoId;
	String type;
	String idx;
	String title;
	String fullTime;
	
	//사용자 개인정보
	String status; // 0 or 1 //학습 완료: 1, 학습 미완료: 0 
	String learningTime;
	
	public static LectureData from(LectureInfo lecture) {
		return LectureData.builder()
				.lectureId(lecture.getLectureId())
				.lectureVideoId(lecture.getLectureVideoId())
				.type(lecture.getType())
				.idx(lecture.getIdx())
				.title(lecture.getTitle())
				.fullTime(lecture.getFullTime())
				.status(lecture.getFullTime())
				.learningTime(lecture.getFullTime())
				.build();
	}
	
}
