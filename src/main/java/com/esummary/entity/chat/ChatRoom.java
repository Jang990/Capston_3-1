package com.esummary.entity.chat;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.esummary.entity.subject.SubjectInfo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	@Setter(value = AccessLevel.PRIVATE)
	private Long id;
	
	@Column(name = "enter_uuid", unique = true)
	private String roomId;
	
	@OneToOne
	@JoinColumn(name = "subject_id")
	private SubjectInfo subject;
	
	@OneToMany(mappedBy = "chatRoom")
	List<ChatMessage> messages;

	public static ChatRoom createRoom(SubjectInfo subject) {
		ChatRoom room = new ChatRoom();
		room.setRoomId(UUID.randomUUID().toString());
		room.setSubject(subject);
//		subject.setChatRoom(room);
		
		return room;
	}
	
	
}
