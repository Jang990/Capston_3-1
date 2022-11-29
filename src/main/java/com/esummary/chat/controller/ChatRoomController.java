package com.esummary.chat.controller;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;  
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		System.out.println("여기"+message);
		sendingOperations.convertAndSend(
				"/topic/chat/room/" + message.getSubjectId(), 
				new ChatMessageDTO(message.getNickname(), message.getMessage(), new Timestamp(System.currentTimeMillis()))
			);
		
		// 채팅 내용 저장
		chatService.saveMessage(message);
	}
	
	@GetMapping("/api/chat/{subjectId}")
	/** 채팅방 입장.(채팅 불러오기) */
	public ResponseEntity<List<ChatMessageDTO>> enterChatRoom(@PathVariable String subjectId) {
		// 채팅 불러오기
		List<ChatMessageDTO> chatList = chatService.loadAllChatMessage(subjectId);
		
		ResponseEntity<List<ChatMessageDTO>> response;
		if(chatList.size() == 0) {
			response = new ResponseEntity<>(chatList, HttpStatus.NO_CONTENT); 
		}
		else {
			response = new ResponseEntity<>(chatList, HttpStatus.OK);
		}
		
		return response;
	}
	
}
