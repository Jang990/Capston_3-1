package com.esummary.crawler.logincrawler;

import java.util.Map;
import java.util.Optional;

public interface LoginCrawler {
    /**
     * 로그인 성공 세션값 받기
     * @param id  이러닝 ID
     * @param password 이러닝 Password
     * @return 이러닝에 로그인 성공한 세션
     */
    Optional<Map<String, String>> getLoginSession(String id, String password) throws Exception;

    /**
     * 해당 세션이 로그인에 성공했는지 확인
     * @param loginId 로그인 계정 ID
     * @param loginSessionCookie loginSession 로그인을 시도한 값
     * @return 성공 여부
     * @throws Exception 이러닝 오류 or 확인 ID와 로그인 ID 불일치
     */
    boolean isSuccessLogin(String loginId, Map<String, String> loginSessionCookie) throws Exception;
}
