package com.esummary.entity.chat;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.esummary.entity.subject.SubjectInfo;
import com.esummary.entity.user.UserInfo;

@Entity
public class ChatMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MSG_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserInfo user;
	
	private String message;
	
	@CreationTimestamp
	private Timestamp createdTime;
	
	@ManyToOne
	@JoinColumn(name = "CHAT_ROOM_ID")
	private SubjectInfo chatRoomSubject;
}
