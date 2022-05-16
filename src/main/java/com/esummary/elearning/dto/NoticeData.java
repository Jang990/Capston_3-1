package com.esummary.elearning.dto;

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
}
