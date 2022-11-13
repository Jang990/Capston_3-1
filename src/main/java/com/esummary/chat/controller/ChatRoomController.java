package com.esummary.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping; 
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.chat.dto.ChatMessageDTO;
import com.esummary.chat.dto.ChatMsgForTestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
	private final SimpMessageSendingOperations sendingOperations;
	
	/*
	 	/receive로 메시지가 들어오면
		"/topic/chat/room/" + message.getRoomId()을 구독한 사용자들에게 
		ChatMsgForTestDTO 객체를 JSON형태로 뿌려준다
	 */
	@MessageMapping("/receive")
//	@SendTo("/topic/chat")
	public void test(ChatMessageDTO message) { 
		sendingOperations.convertAndSend(
				"/topic/chat/room/" + message.getRoomId(), 
				new ChatMsgForTestDTO(message.getSender(), message.getMessage())
			);
		
		// 채팅 내용 저장
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
