package com.esummary.crawler;

import com.esummary.crawler.dto.AnnouncementDTO;
import com.esummary.crawler.dto.AssignmentDTO;
import com.esummary.crawler.dto.CourseDTO;
import com.esummary.crawler.dto.WeekDTO;

import java.util.List;
import java.util.Map;

public class InhaTcCrawler implements Crawler {
    @Override
    public Map<String, String> crawlLoginSession(String id, String password) {
        return null;
    }

    @Override
    public List<CourseDTO> crawlOwnCourse(Map<String, String> loginSession) {
        return null;
    }

    @Override
    public List<WeekDTO> crawlLectureByWeek(Map<String, String> loginSession) {
        return null;
    }

    @Override
    public List<AnnouncementDTO> crawlAnnouncement(Map<String, String> loginSession) {
        return null;
    }

    @Override
    public List<AssignmentDTO> crawlAssignment(Map<String, String> loginSession) {
        return null;
    }
}
