package com.esummary.chat.dto;

import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDTO {

    private String roomId;
    private String roomName;


    public static ChatRoomDTO create(String name) {
        ChatRoomDTO room = new ChatRoomDTO();
        room.roomId = UUID.randomUUID().toString();
        room.roomName = name;
        return room;
    }
}