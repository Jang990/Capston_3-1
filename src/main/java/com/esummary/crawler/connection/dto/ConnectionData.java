package com.esummary.crawler.connection.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;


@Getter
@ToString
public class ConnectionData {
    private String url;
    private Map<String, String> data;
    private Map<String, String> cookies;

    public ConnectionData(String url, Map<String, String> cookies) {
        if(url == null || url.equals("")) {
            throw new IllegalArgumentException("url은 필수 입력 사항입니다. url=" + url);
        }

        this.url = url;
        this.cookies = cookies;
    }

    public ConnectionData(String url, Map<String, String> data, Map<String, String> cookies) {
        if(url == null || url.equals("")) {
            throw new IllegalArgumentException("url은 필수 입력 사항입니다. url=" + url);
        }

        this.url = url;
        this.data = data;
        this.cookies = cookies;
    }
}
