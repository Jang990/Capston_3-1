package com.esummary.chat.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ChatMsgForTestDTO {
	private String userName;
	private String content;
	private Timestamp createdTime;
	
	
	public ChatMsgForTestDTO(String userName, String content) {
		this.userName = userName;
		this.content = content;
	}
	
	@QueryProjection
	public ChatMsgForTestDTO(String userName, String content, Timestamp createdTime) {
		this.userName = userName;
		this.content = content;
		this.createdTime = createdTime;
	}

}