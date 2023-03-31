package com.esummary.crawler.connection.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;


@Getter
@ToString
@AllArgsConstructor
public class ConnectionData {
    private String url;
    private Map<String, String> data;
    private Map<String, String> cookies;

    public ConnectionData(String url, Map<String, String> cookies) {
        this.url = url;
        this.cookies = cookies;
    }
}
