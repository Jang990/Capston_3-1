package com.esummary.crawler.announcement;

import com.esummary.crawler.announcement.dto.AnnouncementDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 세션ID 체크 같은 것들은 하지 않는다.
 * 공지 크롤링만을 시도한다.
 */
public interface AnnouncementCrawler {
    List<AnnouncementDTO> crawlAnnouncement(String courseId, Map<String, String> loginSessionCookie) throws IOException;
}
