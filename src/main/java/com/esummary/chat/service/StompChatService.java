package com.esummary.chat.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.esummary.chat.dto.ChatMsgForTestDTO;
import com.esummary.chat.dto.ChatRoomDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StompChatService {

	private final SimpMessageSendingOperations sendingOperations;
	
    //채팅방 불러오기
    public List<ChatRoomDTO> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
//        List<ChatRoomDTO> result = new ArrayList<>(chatRooms.values());
//        Collections.reverse(result);

        return null;
    }

    //채팅방 하나 불러오기
    public ChatRoomDTO findById(String roomId) {
    	return null;
    }

    //채팅방 생성
    public ChatRoomDTO createRoom(String name) {
        return null;
    }
    
    // 메시지 저장
    public ChatRoomDTO saveMessage() {
    	return null;
    }
    
    public boolean enterChatRoom(String roomId, String userNickname) {
    	sendingOperations.convertAndSend(
				"/topic/chat/room/" + roomId, 
				new ChatMsgForTestDTO("System", userNickname + "님이 입장하셨습니다.")
			);
    	return true;
    }
    
    public boolean exitChatRoom(String roomId, String userNickname) {
    	sendingOperations.convertAndSend(
				"/topic/chat/room/" + roomId, 
				new ChatMsgForTestDTO("System", userNickname + "님이 퇴장하셨습니다.")
			);
    	return true;
    }
}
