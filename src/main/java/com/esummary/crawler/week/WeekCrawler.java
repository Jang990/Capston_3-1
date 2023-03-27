package com.esummary.crawler.week;

import com.esummary.crawler.week.dto.WeekDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WeekCrawler {
    List<WeekDTO> crawlLecture(String courseId, Map<String, String> loginSessionCookie) throws IOException;
}
