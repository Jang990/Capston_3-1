package com.esummary.crawler.connection;

import com.esummary.crawler.connection.dto.ConnectionData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class JsoupPageConnectorTest {
    private PageConnector connector = new JsoupPageConnector();

    @Test
    @DisplayName("이러닝 메인 쿠키 가져오기 테스트")
    public void getCookiesTest() throws Exception {
        //given
        ConnectionData data = new ConnectionData("https://cyber.inhatc.ac.kr/MUser.do", null);

        //when
        Map<String, String> resultCookies = connector.getResultCookies(data);

        //then
        assertThat(resultCookies).isNotEmpty();
        assertThat(resultCookies).containsKey("JSESSIONID");
    }
}