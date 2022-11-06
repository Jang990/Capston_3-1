package com.esummary.entity.chat;

import java.util.List;

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

@Entity
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long id;
	
	@Column(name = "enter_uuid", unique = true)
	private String roomId;
	
	@OneToOne(mappedBy = "chatRoom")
	private SubjectInfo subject;
	
	@OneToMany(mappedBy = "chatRoom")
	List<ChatMessage> messages;
}
