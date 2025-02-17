package com.esummary.crawler.elearning.login;

import java.io.IOException;
import java.util.Map;

public interface LoginCrawler {
    /**
     * 로그인 성공 세션값 받기
     * @param id  이러닝 ID
     * @param password 이러닝 Password
     * @return 이러닝에 로그인 성공한 세션
     * @throws IOException 이외에도 ExpiredELearningSession, MismatchedELearningSessionAndID를 던짐
     */
    Map<String, String> getLoginSession(String id, String password) throws IOException;

    /**
     * 세션으로 로그인한 정보가 로그인 ID와 일치하는지 확인
     * @param loginId 로그인 계정 ID
     * @param loginSessionCookie loginSession 로그인을 시도한 값
     * @return 성공 여부
     * @throws IOException 이외에도 ExpiredELearningSession, MismatchedELearningSessionAndID를 던짐
     */
    boolean validateLoginInfo(String loginId, Map<String, String> loginSessionCookie) throws IOException;

    /**
     * 해당 세션이 만료되었는지 확인
     * @param loginSessionCookie 로그인된 세션
     * @return 만료 여부
     * @throws IOException ExpiredELearningSession도 던짐
     */
    boolean validateExpiredSession(Map<String, String> loginSessionCookie) throws IOException;
}
