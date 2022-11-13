package com.esummary.repository.chat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.chat.dto.ChatMsgForTestDTO;
import com.esummary.entity.chat.ChatMessage;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserInfo;
import com.esummary.repository.subject.SubjectInfoRepository;
import com.esummary.repository.user.UserRepository;

@SpringBootTest
class ChatMessageRepositoryTest {
	
	@Autowired
	private SubjectInfoRepository subjectInfoRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private UserRepository userRepository;
	
	String subjectId = "CORS_220607110428f3327333";
	
	SubjectInfo createSubject() {
		SubjectInfo subject = new SubjectInfo(subjectId, "재학생폭력예방교육(필수)", "류정선", "normal");
		return subjectInfoRepository.save(subject);
	}
	
	UserInfo createUser1() {
		UserInfo user = UserInfo.builder().nickname("테스트계정1").password("test").studentNumber("test001").build();
		return userRepository.save(user);
	}
	
	UserInfo createUser2() {
		UserInfo user = UserInfo.builder().nickname("계정테스트2").password("test").studentNumber("test002").build();
		return userRepository.save(user);
	}
	
	void createMsg(UserInfo user1, UserInfo user2, SubjectInfo subject) {
		for(int i = 0; i < 100; i++) {
			ChatMessage msg = ChatMessage.builder()
					.user(user1)
					.chatRoomSubject(subject)
					.message("메시지-" + i)
					.build();
			try {
				chatMessageRepository.save(msg);
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			ChatMessage msg2 = ChatMessage.builder()
					.user(user2)
					.chatRoomSubject(subject)
					.message("메시지-" + i * 31)
					.build();
			
			try {
				chatMessageRepository.save(msg2);
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	void craeteAll() {
		SubjectInfo subject = createSubject();
		UserInfo user1 = createUser1();
		UserInfo user2 = createUser2();
		createMsg(user1, user2, subject);
	}
	
	@Test
	@Transactional
	void test() {
		craeteAll();
		Pageable pageable = PageRequest.of(0, 30);
		List<ChatMsgForTestDTO> chats = chatMessageRepository.findChatMessage(subjectId, pageable);
		
		for (ChatMsgForTestDTO chatMsgForTestDTO : chats) {
			System.out.println("닉네임:"+ chatMsgForTestDTO.getUserName() + "\t내용: "+chatMsgForTestDTO.getContent() + "\t\t입력시간: " + chatMsgForTestDTO.getCreatedTime());
		}
	}
	
}
