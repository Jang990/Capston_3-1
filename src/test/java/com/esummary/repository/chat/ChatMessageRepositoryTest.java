package com.esummary.repository.chat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.auth.entity.Authority;
import com.esummary.chat.dto.ChatMessageDTO;
import com.esummary.chat.service.StompChatService;
import com.esummary.entity.chat.ChatMessage;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserInfo;
import com.esummary.entity.user.UserSubject;
import com.esummary.repository.UserSubjectRepository;
import com.esummary.repository.subject.SubjectInfoRepository;
import com.esummary.repository.user.UserRepository;

//@SpringBootTest
class ChatMessageRepositoryTest {
	
	@Autowired
	private SubjectInfoRepository subjectInfoRepository;
	@Autowired
	private ChatMessageRepository chatMessageRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private StompChatService chatService;
	
	String subjectId = "CORS_220607110428f3327333";
	
	SubjectInfo createSubject() {
		SubjectInfo subject = new SubjectInfo(subjectId, "재학생폭력예방교육(필수)", "류정선", "normal");
		return subjectInfoRepository.save(subject);
	}
	
	UserInfo createUser1() {
		UserInfo user = UserInfo.builder().nickname("테스트계정1").password("test").studentNumber("test001").roles(Authority.USER).build();
		return userRepository.save(user);
	}
	
	UserInfo createUser2() {
		UserInfo user = UserInfo.builder().nickname("계정테스트2").password("test").studentNumber("test002").roles(Authority.USER).build();
		return userRepository.save(user);
	}
	
	UserInfo createAdmin() {
		UserInfo admin = UserInfo.builder().nickname("System").password("test").studentNumber("System").roles(Authority.ADMIN).build();
		return userRepository.save(admin);
	}
	
	void createMsg(UserInfo user1, UserInfo user2, SubjectInfo subject) {
		for(int i = 0; i < 10; i++) {
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
	
//	@Test
//	@Transactional
	void test() {
		// given
		SubjectInfo subject = createSubject();
		UserInfo user1 = createUser1();
		UserInfo user2 = createUser2();
		createAdmin();
		
		// when
		chatService.enterChatRoom(subjectId, user1.getStudentNumber());
		chatService.enterChatRoom(subjectId, user2.getStudentNumber());
		
		createMsg(user1, user2, subject);
		
		chatService.exitChatRoom(subjectId, user1.getStudentNumber());
		
		Pageable pageable = PageRequest.of(0, 30);
		List<ChatMessageDTO> chats = chatMessageRepository.findChatMessage(subjectId, pageable);
		
		/*
		for (ChatMessageDTO chatMessageDTO : chats) {
			System.out.println("닉네임:"+ chatMessageDTO.getSenderId() + "\t내용: "+chatMessageDTO.getMessage() + "\t\t입력시간: " + chatMessageDTO.getCreatedTime());
		}
		*/
	}

	
	
}
