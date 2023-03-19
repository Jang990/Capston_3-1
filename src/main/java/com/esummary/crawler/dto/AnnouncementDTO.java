package com.esummary.crawler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

// 공지 정보
@Builder
@Getter
@ToString
public class AnnouncementDTO {
    private String announcementId;
    private String title;
    private String author;
    private String announcementDate;
    private String content;
}

