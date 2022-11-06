package com.esummary.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esummary.entity.chat.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>{
	
}
