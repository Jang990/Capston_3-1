package com.esummary.chat.dto;

import lombok.*;

@Getter
@Setter
/** 채팅방으로 채팅 메시지를 보낼 수 있도록 만든 DTO */
public class ChatMessage {
	public enum MessageType{
		 ENTER, // 첫 입장 
		 TALK // 이미 연결
	}

	private MessageType type;
	private String roomId; // 채팅방 ID
	private String sender; // 보내는사람 닉네임
	private String message; // 메시지
}
