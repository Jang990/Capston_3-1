package com.esummary.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.esummary.chat.model.ChatMessageDTO;
import com.esummary.chat.model.ChatMsgForTestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatRoomController {
	
	/*
	 	/receive로 메시지가 들어오면
		/topic/chat을 구독한 사용자들에게 메시지를 뿌려준다
	 */
	@MessageMapping("/receive")
	@SendTo("/topic/chat")
	public ChatMsgForTestDTO test(ChatMessageDTO message) { 
		return new ChatMsgForTestDTO(message.getSender(), message.getMessage());
	}
}
