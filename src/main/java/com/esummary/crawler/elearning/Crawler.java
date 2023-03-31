package com.esummary.crawler.elearning;

import com.esummary.crawler.elearning.announcement.dto.AnnouncementDTO;
import com.esummary.crawler.elearning.assignment.dto.AssignmentDTO;
import com.esummary.crawler.elearning.dto.CourseDTO;
import com.esummary.crawler.elearning.week.dto.WeekDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 이러닝의 정보를 크롤링하는 크롤러.
 * 이러닝의 데이터를 가져오기 위한 용도.
 * 절대 해당 크롤러가 DB에 접근하면 안된다.
 */
public interface Crawler {
    /*
    필요한 행동

    사용자 정보 크롤링 - 필요 없을 수도 있다는 생각이 듦

    로그인 세션 정보 크롤링
    사용자 수강 과목 크롤링
    특정 수업 수업 정보 크롤링
    특정 수업 공지 정보 크롤링
    특정 수업 과제 정보 크롤링
     */

    Map<String, String> crawlLoginSession(String id, String password) throws IOException;
    List<CourseDTO> crawlOwnCourse(String courseId,Map<String,String> loginSessionCookie) throws IOException;
    List<WeekDTO> crawlLectureByWeek(String courseId, Map<String,String> loginSessionCookie) throws IOException;
    List<AnnouncementDTO> crawlAnnouncement(String courseId ,Map<String,String> loginSessionCookie) throws IOException;
    List<AssignmentDTO> crawlAssignment(String courseId, Map<String,String> loginSessionCookie) throws IOException;
}
