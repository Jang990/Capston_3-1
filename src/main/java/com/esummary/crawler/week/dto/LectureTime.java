package com.esummary.crawler.week.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class LectureTime {
    private LocalTime totalTime;
    private LocalTime watchedTime;
}
