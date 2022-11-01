package com.esummary.chat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.esummary.chat.dto.ChatMessage;
import com.esummary.chat.service.ChatRoom;
import com.esummary.chat.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
	private final ObjectMapper objectMapper;
	private final ChatService chatService;
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("{}", payload);
		ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
		
		ChatRoom chatRoom = chatService.findRoomById(chatMessage.getRoomId());
		chatRoom.handlerActions(session, chatMessage, chatService);
	}
}
