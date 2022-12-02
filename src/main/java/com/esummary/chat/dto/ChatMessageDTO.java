package com.esummary.chat.dto;

import java.sql.Timestamp; 

import com.querydsl.core.annotations.QueryProjection;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChatMessageDTO {
	private String senderId;
	private String nickname;
	private String message;
	private String subjectId;
	private Timestamp createdTime;
	
	public ChatMessageDTO(String senderId, String content) {
		this.senderId = senderId;
		this.message = content;
	}
	
	@Builder
	public ChatMessageDTO(String subjectId, String senderId, String message, String nickname) {
		this.subjectId = subjectId;
		this.senderId = senderId;
		this.message = message;
		this.nickname = nickname;
	}
	
	@QueryProjection
	public ChatMessageDTO(String nickname, String content, Timestamp createdTime) {
		this.nickname = nickname;
		this.message = content;
		this.createdTime = createdTime;
	}

	public ChatMessageDTO(String senderId, String message, String subjectId, Timestamp createdTime) {
		this.senderId = senderId;
		this.message = message;
		this.subjectId = subjectId;
		this.createdTime = createdTime;
	}
	
	

}