package com.esummary.repository.chat;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esummary.entity.chat.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>{
	
}
