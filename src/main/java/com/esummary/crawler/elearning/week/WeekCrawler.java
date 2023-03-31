package com.esummary.crawler.elearning.week;

import com.esummary.crawler.elearning.week.dto.WeekDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WeekCrawler {
    List<WeekDTO> crawlLecture(String courseId, Map<String, String> loginSessionCookie) throws IOException;
}
