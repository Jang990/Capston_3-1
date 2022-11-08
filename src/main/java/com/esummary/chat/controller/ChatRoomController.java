package com.esummary.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping; 
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.chat.model.ChatMessageDTO;
import com.esummary.chat.model.ChatMsgForTestDTO;

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
	}
}
