package com.esummary.crawler;

import com.esummary.crawler.announcement.AnnouncementCrawler;
import com.esummary.crawler.announcement.dto.AnnouncementDTO;
import com.esummary.crawler.assignment.dto.AssignmentDTO;
import com.esummary.crawler.dto.CourseDTO;
import com.esummary.crawler.week.dto.WeekDTO;
import com.esummary.crawler.login.LoginCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InhatcCrawler implements Crawler {
//    public final String MAIN_URL = "https://cyber.inhatc.ac.kr";

    private final LoginCrawler inhatcLoginCrawler;
    private final AnnouncementCrawler inhatcAnnouncementCrawler;

    @Override
    public Map<String, String> crawlLoginSession(String id, String password) throws IOException {
        return inhatcLoginCrawler.getLoginSession(id, password);
    }

    @Override
    public List<CourseDTO> crawlOwnCourse(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        return null;
    }

    @Override
    public List<WeekDTO> crawlLectureByWeek(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        return null;
    }

    @Override
    public List<AnnouncementDTO> crawlAnnouncement(String courseId, Map<String, String> loginSessionCookie) throws IOException{
        return inhatcAnnouncementCrawler.crawlAnnouncement(courseId, loginSessionCookie);
    }

    @Override
    public List<AssignmentDTO> crawlAssignment(String courseId, Map<String, String> loginSessionCookie) throws IOException {
        return null;
    }
}
