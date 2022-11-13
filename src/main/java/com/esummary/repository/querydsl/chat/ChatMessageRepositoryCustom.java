package com.esummary.repository.querydsl.chat;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.esummary.chat.dto.ChatMsgForTestDTO;

public interface ChatMessageRepositoryCustom {
	List<ChatMsgForTestDTO> findChatMessage(String subjectId, Pageable pageable);
}
