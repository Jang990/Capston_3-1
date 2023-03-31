package com.esummary.crawler.connection;

import com.esummary.crawler.connection.dto.ConnectionData;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public interface PageConnector {
    /**
     * GET 요청
     */
    Document getContent(ConnectionData connectionData) throws IOException;

    /**
     * POST 요청
     */
    Document postContent(ConnectionData connectionData) throws IOException;

    /**
     * GET 결과 쿠키 가져오기
     */
    Map<String, String> getResultCookies(ConnectionData connectionData) throws IOException;
}
