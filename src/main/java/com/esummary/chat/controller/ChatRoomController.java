package com.esummary.chat.controller;

import java.sql.Timestamp;

import org.springframework.messaging.handler.annotation.MessageMapping;  
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.chat.dto.ChatMessageDTO;
import com.esummary.chat.service.StompChatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
	private final SimpMessageSendingOperations sendingOperations;
	private final StompChatService chatService;
	
	/*
	 	/receive로 메시지가 들어오면
		"/topic/chat/room/" + message.getRoomId()을 구독한 사용자들에게 
		ChatMsgForTestDTO 객체를 JSON형태로 뿌려준다
	 */
	@MessageMapping("/receive")
//	@SendTo("/topic/chat")
	public void test(ChatMessageDTO message) {
		sendingOperations.convertAndSend(
				"/topic/chat/room/" + message.getSubjectId(), 
				new ChatMessageDTO(message.getSenderId(), message.getMessage(), new Timestamp(System.currentTimeMillis()))
			);
		
		// 채팅 내용 저장
		chatService.saveMessage(message);
	}
	
	@GetMapping("/api/chat/{subjectId}")
	/** 채팅방 입장.(채팅 불러오기) */
	public void enterChatRoom(ChatMessageDTO message) {
		// 채팅 불러오기
	}
	
	// 채팅방 DB 저장 및 불러오기 구현
	// 
	// 채팅방 입장 구현 - 끝
	// 
	// 퇴장 구현 - 끝
}
