package com.esummary.elearning.dto;

import com.esummary.elearning.entity.subject.NoticeInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeData {
	private String noticeId;
	private String title;
	private String description;
	private String author;
	private String createDate;
	
	public static NoticeData convertNoticeData(NoticeInfo subjectNoticeInfo) {
		return new NoticeData(
				subjectNoticeInfo.getNoticeId(), 
				subjectNoticeInfo.getTitle(), 
				subjectNoticeInfo.getDescription(), 
				subjectNoticeInfo.getAuthor(), 
				subjectNoticeInfo.getCreateDate()
		);
	}
}
