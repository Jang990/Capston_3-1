package com.esummary.crawler.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ContentDetail {
    private String id;
    private String title;
    private String content;

    public ContentDetail(String id, String title, String content) {
        if(id == null || id.equals("")) {
            throw new IllegalArgumentException("Content의 ID는 필수입니다.");
        }

        this.id = id;
        this.title = title;
        this.content = content;
    }
}
