package com.esummary.crawler.lecture;

import com.esummary.crawler.lecture.dto.WeekDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface LectureCrawler {
    List<WeekDTO> crawlLecture(String courseId, Map<String, String> loginSessionCookie) throws IOException;
}
