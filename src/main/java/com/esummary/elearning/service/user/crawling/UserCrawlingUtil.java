package com.esummary.elearning.service.user.crawling;

import java.util.Map;

public interface UserCrawlingUtil {
	/*
	 * 사용자의 개인정보를 가져오는 유틸
	 * 예를들어 사용자 이름. 몇학년인지. 학점이 어떤지. 이런것을 가져올 때 사용될 예정.
	 */
	
	
	String getUserName(Map<String, String> loginCookies);
}
