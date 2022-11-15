package com.esummary.chat.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.auth.exception.NotFoundMemberException;
import com.esummary.chat.dto.ChatMessageDTO;
import com.esummary.entity.chat.ChatMessage;
import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserInfo;
import com.esummary.entity.user.UserSubject;
import com.esummary.repository.UserSubjectRepository;
import com.esummary.repository.chat.ChatMessageRepository;
import com.esummary.repository.subject.SubjectInfoRepository;
import com.esummary.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StompChatService {

	private final SimpMessageSendingOperations sendingOperations;
	private final ChatMessageRepository chatMessageRepository;
	private final UserRepository userRepository;
	private final SubjectInfoRepository subjectRepository;
	private final UserSubjectRepository userSubjectRepository;
	
    //채팅방 불러오기
    public List<ChatMessageDTO> loadAllChatMessage(String subjectId) {
    	List<ChatMessageDTO> chatList = chatMessageRepository.findChatMessage(subjectId, PageRequest.of(0, 30));
    	System.out.println(chatList.size());
        return chatList;
    }
    
    // 메시지 저장
    public ChatMessageDTO saveMessage(ChatMessageDTO chatDTO) {
    	UserInfo user = userRepository.findByStudentNumber(chatDTO.getSenderId()).orElseThrow(
    				() -> new NotFoundMemberException("학생을 찾을 수 없습니다. StudentNumber: " + chatDTO.getSenderId())
    			);
    	SubjectInfo subject = subjectRepository.findBySubjectId(chatDTO.getSubjectId()).orElseThrow(
    				() -> new IllegalArgumentException("과목을 찾을 수 없습니다. SubjectID: " + chatDTO.getSubjectId())
    			);
    	
    	ChatMessage chat = ChatMessage.builder()
    			.chatRoomSubject(subject).user(user).message(chatDTO.getMessage()).build();
    	
    	chat = chatMessageRepository.save(chat);
    	
    	return ChatMessageDTO.builder()
    			.subjectId(subject.getSubjectId())
    			.senderId(user.getStudentNumber())
    			.message(chat.getMessage())
    			.build();
    }
    
    /** 
     * 채팅방 입장 메시지<br> 
     * 채팅방 입장은 크롤링했을 때 UserSubject 테이블에 사용자의 데이터가 없으면 입장 
     */
    public ChatMessageDTO enterChatRoom(String roomId, String studentNumber) {
    	UserInfo user = userRepository.findByStudentNumber(studentNumber).get();
    	SubjectInfo subject = subjectRepository.findBySubjectId(roomId).orElseThrow(
				() -> new IllegalArgumentException("과목을 찾을 수 없습니다. SubjectID: " + roomId)
			);
    	
    	String message = user.getNickname() + "님이 입장하셨습니다.";
    	ChatMessageDTO msg = ChatMessageDTO.builder()
    			.senderId("System").subjectId(roomId).message(message).build();
    	sendingOperations.convertAndSend("/topic/chat/room/" + roomId, msg);
    	
    	// 채팅방 입장
    	if(userSubjectRepository.findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(roomId, studentNumber).isPresent()) {
    		throw new IllegalArgumentException("이미 채팅방에 입장해있습니다. \n"
    											+ "SubjectId: " + roomId + "\t StudentId: " + studentNumber);
    	}
    	UserSubject us = new UserSubject(user, subject);
    	us = userSubjectRepository.save(us);
    	
    	return saveMessage(msg);
    }
    
    /** 
     * 채팅방 퇴장 메시지<br> 
     * 채팅방 입장은 크롤링했을 때 UserSubject 테이블에 사용자 데이터가 있으나 크롤링 데이터가 없으면 퇴장 
     */
    public ChatMessageDTO exitChatRoom(String roomId, String studentNumber) {
    	UserInfo user = userRepository.findByStudentNumber(studentNumber).get();
    	String message = user.getNickname() + "님이 퇴장하셨습니다.";
    	ChatMessageDTO msg = ChatMessageDTO.builder()
    			.senderId("System").subjectId(roomId).message(message).build();
    	
    	sendingOperations.convertAndSend("/topic/chat/room/" + roomId, msg);
    	
    	// 채팅방 나가기
    	UserSubject exitRoom = userSubjectRepository.findBySubjectInfo_SubjectIdAndUserInfo_StudentNumber(roomId, studentNumber)
    			.orElseThrow(() -> new IllegalArgumentException("채팅방에 입장해있지 않습니다. \n"
    															+ "SubjectId: " + roomId + "\t StudentId: " + studentNumber));
    	userSubjectRepository.delete(exitRoom);
//    	userSubjectRepository.deleteByUserInfo_StudentNumberAndSubjectInfo_SubjectId(studentNumber, roomId); // 오류남 
    	
    	
    	return saveMessage(msg);
    }
}
