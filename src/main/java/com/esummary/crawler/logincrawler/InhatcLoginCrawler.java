package com.esummary.crawler.logincrawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@Slf4j
public class InhatcLoginCrawler implements LoginCrawler {
    private final String INIT_COOKIE_URL = "https://cyber.inhatc.ac.kr/MMain.do?cmd=viewIndexPage";
    private final String LOGIN_URL = "https://cyber.inhatc.ac.kr/MUser.do";

    @Override
    public Optional<Map<String, String>> getLoginSession(String id, String password) throws Exception {
        if(id == null || password == null)
            throw new IllegalArgumentException();

        Optional<Map<String, String>> loginSessionCookie = attemptToLogin(id, password, this.getInitCookie());

        if(loginSessionCookie.isEmpty())
            return Optional.empty();

        if(validateLoginInfo(id, loginSessionCookie.get()))
            return loginSessionCookie;
        else
            return Optional.empty();
    }

    @Override
    public boolean validateLoginInfo(String loginId, Map<String, String> loginSessionCookie) throws Exception {
        Document loginPage = connLoginPage(loginSessionCookie);

        if(validateExpiredSession(loginSessionCookie)) {
            return false;
        }
        Element str = loginPage.getElementsByClass("login_info").select("ul li").last(); //정보를 찾을 수 없음. 즉 로그인이 되지 않은 쿠키라는 것(또는 만료된 로그인 쿠키라는 것)
        if(str == null) {
        }

        String[] nameAndWStudentNumber = str.text().split(" ");
        if(nameAndWStudentNumber.length < 2) {
            log.warn("로그인한 사용자 ID를 HTML 문서상에서 찾을 수 없음");
            return false;
        }

        String studentName = nameAndWStudentNumber[1].substring(1, nameAndWStudentNumber[1].length()-1);
        if(!studentName.equals(loginId)) {
            // 시도한 학생 ID와 로그인 세션의 ID가 다름 - 가장 심각한 문제
            log.warn(loginId+"정보와 로그인에 성공한 세션 정보가 일치하지 않음");
            throw new Exception();
        }

        return true;
    }

    @Override
    public boolean validateExpiredSession(Map<String, String> loginSessionCookie) throws Exception {
        Document loginPage = connLoginPage(loginSessionCookie);
        System.out.println("loginPage = " + loginPage);
        Element str = loginPage.getElementsByClass("login_info").select("ul li").last();

        //정보를 찾을 수 없음. 즉 로그인이 되지 않은 쿠키라는 것(또는 만료된 로그인 쿠키라는 것)
        if(str == null) {
            log.info("세션 쿠키 정보가 만료되었거나 올바르지 않음 - {}", loginSessionCookie);
            return false;
        }

        return true;
    }

    // 로그인 성공 페이지로 이동
    private Document connLoginPage(Map<String, String> loginSessionCookie) throws IOException {
        Document loginPage = null;
        Connection connectionLoginPage = Jsoup.connect(INIT_COOKIE_URL)
                .data("cmd", "viewIndexPage")
                .cookies(loginSessionCookie);
        Connection.Response resp = connectionLoginPage.execute(); // IOException 발생 가능

        if(resp.statusCode() == 200) {
            loginPage = connectionLoginPage.post();
        }

        return loginPage;
    }

    // 로그인 시도를 위한 초기 쿠키 세팅
    private Map<String, String> getInitCookie() throws IOException {
        // 초기 쿠키 가져오기
        Connection.Response initial = Jsoup.connect(INIT_COOKIE_URL)
                .method(Connection.Method.GET)
                .execute();
        return initial.cookies();
    }

    // 로그인 시도 후 로그인 성공 쿠키 반환
    private Optional<Map<String, String>> attemptToLogin(String id, String password, Map<String, String> initialCookies) throws Exception {
        Connection con = Jsoup.connect(LOGIN_URL)
                .data("cmd", "loginUser")
                .data("userDTO.userId", id)
                .data("userDTO.password", password)
                .data("userDTO.localeKey", "ko")
                .cookies(initialCookies);

        Connection.Response resp = con.execute();
        if(resp.statusCode() != 200) {
            return Optional.empty();
        }
        else {
            con.post();
            return Optional.ofNullable(initialCookies);
        }
    }
}
