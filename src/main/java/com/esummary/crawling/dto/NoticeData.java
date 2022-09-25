package com.esummary.crawling.dto;

import com.esummary.elearning.entity.subject.NoticeInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoticeData {
	private String noticeId;
	private String title;
	private String description;
	private String author;
	private String createDate;
	
	public static NoticeData exConvertNoticeData(NoticeInfo subjectNoticeInfo) {
		return new NoticeData(
				subjectNoticeInfo.getNoticeId(), 
				subjectNoticeInfo.getTitle(), 
				subjectNoticeInfo.getDescription(), 
				subjectNoticeInfo.getAuthor(), 
				subjectNoticeInfo.getCreateDate()
		);
	}
	
	public static NoticeData from(NoticeInfo notice) {
		if(notice == null) return null;
		
		return NoticeData.builder()
				.noticeId(notice.getNoticeId())
				.title(notice.getTitle())
				.description(notice.getDescription())
				.author(notice.getAuthor())
				.createDate(notice.getCreateDate())
				.build();
	}
}
