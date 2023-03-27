package com.esummary.crawler.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ContentPeriod {
    private LocalDateTime from; // 시작 날짜
    private LocalDateTime to; // 종료 날짜

    public ContentPeriod(LocalDateTime from, LocalDateTime to) {
        if(from.isAfter(to)) {
            throw new IllegalArgumentException("종료날짜는 시작날짜보다 빠를 수 없습니다.");
        }

        this.from = from;
        this.to = to;
    }
}
