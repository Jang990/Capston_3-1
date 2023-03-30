package com.esummary.crawler.week.lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class LectureTime {
    private LocalTime totalTime;
    private LocalTime watchedTime;
}
