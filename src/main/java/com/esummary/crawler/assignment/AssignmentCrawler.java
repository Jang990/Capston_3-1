package com.esummary.crawler.assignment;

import com.esummary.crawler.assignment.dto.AssignmentDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 세션ID 체크 같은 것들은 하지 않는다.
 * 그냥 과제 크롤링만을 시도한다.
 */
public interface AssignmentCrawler {
    List<AssignmentDTO> crawlAssignment(String courseId, Map<String, String> loginSessionCookie) throws IOException;
}
