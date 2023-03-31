package com.esummary.crawler.elearning.week.lecture.dto;

import com.esummary.crawler.elearning.dto.ContentCompletionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

// 실제 수업 정보
@Getter
@ToString
@AllArgsConstructor
public class LectureDTO {
    private int idx; // Week에서 Lecture의 idx는 중복되면 안된다.
    private LectureType type;
    private String title;
    private LectureTime time;
    private ContentCompletionStatus status;

}
