package com.esummary.elearning.service.login.util.login;

import java.util.Map;

import org.jsoup.nodes.Document;

import com.esummary.elearning.entity.user.UserInfo;

public interface LoginUtil {
	Map<String, String> getInitialCookies(String loginParameterUrl);
	boolean loginToUsingUserData(String id, String password, UserInfo user);
	Document conLoginPage(Map<String, String> initialCookies);
//	UserInfo getUserInfo();
	
//	Map<String, String> getInitialCookies();
}
