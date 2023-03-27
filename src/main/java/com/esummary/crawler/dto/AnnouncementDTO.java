package com.esummary.crawler.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

// 공지 정보
@Getter
@ToString
@AllArgsConstructor
public class AnnouncementDTO {
    private ContentDetail contentDetail;
    private LocalDateTime announcementDate;
}

