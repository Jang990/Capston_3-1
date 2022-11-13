package com.esummary.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping; 
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.esummary.chat.dto.ChatMessageDTO;

import lombok.RequiredArgsConstructor;

//@RestController
@RequiredArgsConstructor
public class exChatController {
	@MessageMapping("/chatHello")
	@SendTo("topic/members")
	public String Test() {
		return "안녕";
	}
	
    private final SimpMessagingTemplate template; //특정 Broker로 메세지를 전달
//    private final ChatRepository cr;
//    private final ChatRoomRepository crr;
//    private final ChatService cs;

    //Client 가 SEND 할 수 있는 경로
    //stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO message) {
        message.setMessage(message.getSender() + "님이 채팅방에 참여하였습니다.");


//        List<ChatMessageDetailDTO> chatList = cs.findAllChatByRoomId(message.getRoomId());
//        if(chatList != null){
//             for(ChatMessageDetailDTO c : chatList ){
//                 message.setWriter(c.getWriter());
//                 message.setMessage(c.getMessage());
//             }
//         }

        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

//        ChatRoomEntity chatRoomEntity= crr.findByRoomId(message.getRoomId());
//        ChatMessageSaveDTO chatMessageSaveDTO = new ChatMessageSaveDTO(message.getRoomId(),message.getWriter(), message.getMessage());
//        cr.save(ChatMessageEntity.toChatEntity(chatMessageSaveDTO,chatRoomEntity));
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO message) {
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);

        // DB에 채팅내용 저장
//        ChatRoomEntity chatRoomEntity= crr.findByRoomId(message.getRoomId());
//        ChatMessageSaveDTO chatMessageSaveDTO = new ChatMessageSaveDTO(message.getRoomId(),message.getWriter(), message.getMessage());
//        cr.save(ChatMessageEntity.toChatEntity(chatMessageSaveDTO,chatRoomEntity));
    }
}
