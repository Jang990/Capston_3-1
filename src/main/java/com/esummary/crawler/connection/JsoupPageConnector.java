package com.esummary.crawler.connection;

import com.esummary.crawler.connection.dto.ConnectionData;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class JsoupPageConnector implements PageConnector {

    @Override
    public Document getContent(ConnectionData connectionData) throws IOException {
        Connection conn = createConnection(connectionData);
        return conn.get();
    }

    @Override
    public Document postContent(ConnectionData connectionData) throws IOException {
        Connection conn = createConnection(connectionData);
        return conn.post();
    }

    @Override
    public Map<String, String> getResultCookies(ConnectionData connectionData) throws IOException {
        Connection conn = createConnection(connectionData);
        return conn.method(Connection.Method.GET)
                .execute().cookies();
    }

    private Connection createConnection(ConnectionData connectionData) {
        Connection conn = Jsoup.connect(connectionData.getUrl());

        if(connectionData.getCookies() != null) {
            conn.cookies(connectionData.getCookies());
        }

        Map<String, String> data = connectionData.getData();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            conn.data(entry.getKey(), entry.getValue());
        }

        return conn;
    }
}
