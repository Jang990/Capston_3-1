package com.esummary.crawler;

import java.util.Map;

public class InhatcCrawlerConfig {
    public static final String id = "201845096";
    public static final String password =
            state.EMPTY.toString();

    public static final String failPassword = "abcde12345";

    public static final Map<String, String> failSessionCookie = Map.of("SessionId" , "failSessionID");
    public static final String courseId = "202224043DMP636";


    public static enum state {
        EMPTY;
    }
}
