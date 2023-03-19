package com.esummary.crawler.announcementcrawler;

import com.esummary.crawler.dto.AnnouncementDTO;

import java.util.List;
import java.util.Map;

public interface AnnouncementCrawler {
    List<AnnouncementDTO> crawlAnnouncement(String courseId, Map<String, String> loginSessionCookie);
}
