package com.esummary.crawler.logincrawler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class InhatcLoginCrawlerTest {

    private final LoginCrawler loginCrawler = new InhatcLoginCrawler();
    private String id = "201845096";
    private String password = ".";
    private String failPassword = "1111";

    @Test
    @DisplayName("로그인 세션ID 가져오기")
    void getLoginSession() throws Exception {
        // when
        Optional<Map<String, String>> loginSession = loginCrawler.getLoginSession(id, password);

        // then
        assertThat(loginSession.isPresent()).isEqualTo(true);
    }

    @Test
    @DisplayName("로그인 세션ID 가져오기 실패")
    void getLoginSessionFail() throws Exception {

        // when
        Optional<Map<String, String>> loginSession = loginCrawler.getLoginSession(id, failPassword);

        // then
        assertThat(loginSession.isEmpty()).isEqualTo(true);
    }

    @Test
    @DisplayName("올바르지 않은 세션 ID 검증 실패")
    void isSuccessLoginFail() throws Exception {
        // given
        Map<String, String> loginCookie = new HashMap<>();
        loginCookie.put("JSESSIONID", "something-wrong");

        // when
        boolean successLogin = loginCrawler.isSuccessLogin(id, loginCookie);

        //then
        assertThat(successLogin).isEqualTo(false);
    }

    @Test
    @DisplayName("로그인한 아이디와 세션을 통해 불러온 정보 불일치")
    void isLoginInfoMismatch() throws Exception {
        // given
        Map<String, String> loginCookie =
                loginCrawler.getLoginSession(id, password).orElseThrow(() -> new Exception());
        String modifiedId = "wrongId";

        // when, then
        assertThrows(Exception.class, () -> loginCrawler.isSuccessLogin(modifiedId, loginCookie));
    }
}