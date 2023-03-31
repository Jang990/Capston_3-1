package com.esummary.crawler.elearning.week.dto;

import com.esummary.crawler.elearning.dto.ContentDetail;
import com.esummary.crawler.elearning.dto.ContentPeriod;
import com.esummary.crawler.elearning.week.lecture.dto.LectureDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
// 주차 정보
public class WeekDTO {
    ContentDetail contentDetail;
    ContentPeriod contentPeriod;
    List<LectureDTO> lectureList;
}
