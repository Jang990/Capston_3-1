package com.esummary.crawler.logincrawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
public class InhatcLoginCrawler implements LoginCrawler {
    private final String INIT_COOKIE_URL = "https://cyber.inhatc.ac.kr/MMain.do?cmd=viewIndexPage";
    private final String LOGIN_URL = "https://cyber.inhatc.ac.kr/MUser.do";

    @Override
    public Optional<Map<String, String>> getLoginSession(String id, String password) throws Exception {
        Optional<Map<String, String>> loginSessionCookie = attemptToLogin(id, password, this.getInitCookie());

        if(loginSessionCookie.isEmpty())
            return Optional.empty();

        if(isSuccessLogin(id, loginSessionCookie.get()))
            return loginSessionCookie;
        else
            return Optional.empty();
    }

    @Override
    public boolean isSuccessLogin(String loginId, Map<String, String> loginSessionCookie) throws Exception {
        Document loginPage = connLoginPage(loginSessionCookie);

        // 로그인을 해서 이름이 나오는지 체크
        Element str = loginPage.getElementsByClass("login_info").select("ul li").last(); //정보를 찾을 수 없음. 즉 로그인이 되지 않은 쿠키라는 것(또는 만료된 로그인 쿠키라는 것)
        if(str == null)
            return false; // 로그인 실패

        String[] nameAndWStudentNumber = str.text().split(" ");
        if(nameAndWStudentNumber.length < 2)
            return false;

        String studentName = nameAndWStudentNumber[1].substring(1, nameAndWStudentNumber[1].length()-1);
        System.out.println("studentName = " + studentName);
        if(!studentName.equals(loginId)) {
            // 시도한 학생 ID와 로그인 세션의 ID가 다름 - 가장 심각한 문제
            throw new Exception();
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

        if(resp.statusCode() == 200)
            loginPage = connectionLoginPage.post();

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
