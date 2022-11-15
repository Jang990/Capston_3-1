package com.esummary.repository.querydsl.chat;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.esummary.chat.dto.ChatMessageDTO;

public interface ChatMessageRepositoryCustom {
	List<ChatMessageDTO> findChatMessage(String subjectId, Pageable pageable);
}
