package com.esummary.repository.querydsl.chat;

import static com.esummary.entity.chat.QChatMessage.chatMessage;
import static com.esummary.entity.user.QUserInfo.userInfo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.esummary.chat.dto.ChatMsgForTestDTO;
import com.esummary.chat.dto.QChatMsgForTestDTO;
import com.esummary.entity.chat.ChatMessage;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {
	
	private final JPAQueryFactory query;
	
	@Override
	public List<ChatMsgForTestDTO> findChatMessage(String subjectId, Pageable pageable) {
		return query.select(
				new QChatMsgForTestDTO(
						userInfo.nickname, 
						chatMessage.message, 
						chatMessage.createdTime
					) 
			)
			.from(chatMessage)
			.innerJoin(chatMessage.user, userInfo)
			.where(chatMessage.chatRoomSubject.subjectId.eq(subjectId))
			.orderBy(chatMessage.createdTime.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}
	
}
