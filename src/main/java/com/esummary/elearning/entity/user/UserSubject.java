package com.esummary.elearning.entity.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.WeekInfo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@NamedEntityGraphs({
	@NamedEntityGraph(
			name = "user-own-task",
			attributeNodes = {
					@NamedAttributeNode(value = "userTask", subgraph = "task-detail")
			},
			subgraphs = { @NamedSubgraph(
					name = "task-detail",
					attributeNodes = { @NamedAttributeNode("taskInfo") } 
				)
			}
	),
	@NamedEntityGraph(
			name = "user-lecture-week",
			attributeNodes = {
					@NamedAttributeNode(value = "subjectInfo", subgraph = "week-detail")
			},
			subgraphs = { 
				@NamedSubgraph(
					name = "week-detail",
					attributeNodes = { @NamedAttributeNode("lectureList") } 
				),
			}
	)
})
@Data
@Entity
public class UserSubject {
	
	@Setter(AccessLevel.NONE)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long usId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="STUDENT_NUMBER")
	private UserInfo userInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="SUBJECT_ID")
	private SubjectInfo subjectInfo;
	
	@OneToMany(mappedBy = "userSubject")
	private List<UserTask> userTask;
	
	@OneToMany(mappedBy = "userSubject")
	private List<UserLecture> userLecture;
	
	public UserSubject() {
		this.userInfo = new UserInfo();
		this.subjectInfo = new SubjectInfo();
		this.userTask = new ArrayList<UserTask>();
		this.userLecture = new ArrayList<UserLecture>();
	}
	
	public UserSubject(UserInfo userInfo, SubjectInfo subjectInfo) {
		this.userInfo = userInfo;
		this.subjectInfo = subjectInfo;
	}
	
	public String getStudentNumber() {
		return this.userInfo.getStudentNumber();
	}
	public String getSubjectId() {
		return this.subjectInfo.getSubjectId();
	}
	public void setStudentNumber(String number) {
		this.userInfo.setStudentNumber(number);
	}
	public void setSubjectId(String id) {
		this.subjectInfo.setSubjectId(id);
	}
}
