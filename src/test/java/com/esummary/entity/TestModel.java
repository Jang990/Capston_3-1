package com.esummary.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Entity
@Data
public class TestModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long testId;
	
	private String name;
	
	@CreationTimestamp
	private Timestamp createTime;
	@UpdateTimestamp
	private Timestamp crawlingTime;
	
}
