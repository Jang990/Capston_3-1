package com.esummary.chat.service;

import java.util.ArrayList; 
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.auth.exception.NotFoundMemberException;
import com.esummary.chat.dto.ChatMessageDTO;
import com.esummary.chat.dto.ChatMsgForTestDTO;
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
    public List<ChatMsgForTestDTO> findAllRoom() {
        //채팅방 최근 생성 순으로 반환
//        List<ChatRoomDTO> result = new ArrayList<>(chatRooms.values());
//        Collections.reverse(result);

        return null;
    }

    //채팅방 하나 불러오기
    public ChatMsgForTestDTO findById(String roomId) {
    	return null;
    }

    //채팅방 생성
    public ChatMsgForTestDTO createRoom(String name) {
        return null;
    }
    
    // 메시지 저장
    public ChatMsgForTestDTO saveMessage(ChatMsgForTestDTO chatDTO) {
    	UserInfo user = userRepository.findByStudentNumber(chatDTO.getUserName()).orElseThrow(
    				() -> new NotFoundMemberException("학생을 찾을 수 없습니다. StudentNumber: " + chatDTO.getUserName())
    			);
    	SubjectInfo subject = subjectRepository.findBySubjectId(chatDTO.getSubjectId()).orElseThrow(
    				() -> new IllegalArgumentException("과목을 찾을 수 없습니다. SubjectID: " + chatDTO.getSubjectId())
    			);
    	
    	ChatMessage chat = ChatMessage.builder()
    			.chatRoomSubject(subject).user(user).message(chatDTO.getContent()).build();
    	
    	chat = chatMessageRepository.save(chat);
    	
    	return ChatMsgForTestDTO.builder()
    			.subjectId(subject.getSubjectId())
    			.userName(user.getStudentNumber())
    			.content(chat.getMessage())
    			.build();
    }
    
    /** 
     * 채팅방 입장 메시지<br> 
     * 채팅방 입장은 크롤링했을 때 UserSubject 테이블에 사용자의 데이터가 없으면 입장 
     */
    public ChatMsgForTestDTO enterChatRoom(String roomId, String studentNumber) {
    	UserInfo user = userRepository.findByStudentNumber(studentNumber).get();
    	SubjectInfo subject = subjectRepository.findBySubjectId(roomId).orElseThrow(
				() -> new IllegalArgumentException("과목을 찾을 수 없습니다. SubjectID: " + roomId)
			);
    	
    	String message = user.getNickname() + "님이 입장하셨습니다.";
    	ChatMsgForTestDTO msg = ChatMsgForTestDTO.builder()
    			.userName("System").subjectId(roomId).content(message).build();
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
    public ChatMsgForTestDTO exitChatRoom(String roomId, String studentNumber) {
    	UserInfo user = userRepository.findByStudentNumber(studentNumber).get();
    	String message = user.getNickname() + "님이 퇴장하셨습니다.";
    	ChatMsgForTestDTO msg = ChatMsgForTestDTO.builder()
    			.userName("System").subjectId(roomId).content(message).build();
    	
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
