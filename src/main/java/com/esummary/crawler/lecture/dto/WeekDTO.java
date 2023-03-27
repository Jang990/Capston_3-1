package com.esummary.crawler.lecture.dto;

import com.esummary.crawler.dto.ContentDetail;
import com.esummary.crawler.dto.ContentPeriod;
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
