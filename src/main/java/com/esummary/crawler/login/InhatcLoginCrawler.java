package com.esummary.crawler.login;

import com.esummary.crawler.login.exception.ExpiredELearningSession;
import com.esummary.crawler.login.exception.MismatchedELearningSessionAndID;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class InhatcLoginCrawler implements LoginCrawler {
    private final String INIT_COOKIE_URL = "https://cyber.inhatc.ac.kr/MMain.do?cmd=viewIndexPage";
    private final String LOGIN_URL = "https://cyber.inhatc.ac.kr/MUser.do";

    @Override
    public Map<String, String> getLoginSession(String id, String password) throws IOException {
        Map<String, String> loginSessionCookie = attemptToLogin(id, password, this.getInitCookie());
        validateExpiredSession(loginSessionCookie);
        return loginSessionCookie;
    }

    @Override
    public boolean validateLoginInfo(String loginId, Map<String, String> loginSessionCookie) throws IOException {
        Document loginPage = connLoginPage(loginSessionCookie);
        validateExpiredSession(loginSessionCookie);

        Element str = loginPage.getElementsByClass("login_info").select("ul li").last();

        String[] nameAndWStudentNumber = str.text().split(" ");
        if(nameAndWStudentNumber.length < 2) {
            throw new IOException("InhatcLoginCrawler.validateLoginInfo 의도한 크롤링 로직 실패");
        }

        String studentName = nameAndWStudentNumber[1].substring(1, nameAndWStudentNumber[1].length()-1);
        if(!studentName.equals(loginId)) {
            // 시도한 학생 ID와 로그인 세션의 ID가 다름 - 가장 심각한 문제
            throw new MismatchedELearningSessionAndID("시도한 학생 ID와 로그인 세션의 ID가 다름 { Session = "+loginSessionCookie);
        }

        return true;
    }

    @Override
    public boolean validateExpiredSession(Map<String, String> loginSessionCookie) throws IOException {
        Document loginPage = connLoginPage(loginSessionCookie);
        Element str = loginPage.getElementsByClass("login_info").select("ul li").last();

        //정보를 찾을 수 없음. 즉 로그인이 되지 않은 쿠키라는 것(또는 만료된 로그인 쿠키라는 것)
        if(str == null) {
            throw new ExpiredELearningSession("세션 쿠키 정보가 만료되었거나 올바르지 않음 - Session = "+loginSessionCookie);
        }

        String[] nameAndWStudentNumber = str.text().split(" ");
        if(nameAndWStudentNumber.length < 2) {
            throw new ExpiredELearningSession("세션 쿠키 정보가 만료되었거나 올바르지 않음 - Session = "+loginSessionCookie);
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
    private Map<String, String> attemptToLogin(String id, String password, Map<String, String> initialCookies) throws IOException {
        Connection con = Jsoup.connect(LOGIN_URL)
                .data("cmd", "loginUser")
                .data("userDTO.userId", id)
                .data("userDTO.password", password)
                .data("userDTO.localeKey", "ko")
                .cookies(initialCookies);

        con.post();
        return initialCookies;
    }
}
