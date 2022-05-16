package com.esummary.elearning.entity.user;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;

import com.esummary.elearning.entity.subject.SubjectInfo;
import com.esummary.elearning.entity.subject.SubjectLectureWeekInfo;

import lombok.Data;

@NamedEntityGraphs({
	@NamedEntityGraph(
			name = "user-own-task",
			attributeNodes = {
					@NamedAttributeNode(value = "userTask", subgraph = "task-detail")
			},
			subgraphs = { @NamedSubgraph(
					name = "task-detail",
					attributeNodes = { @NamedAttributeNode("subjectTaskInfo") } 
				)
			}
	),
	@NamedEntityGraph(
			name = "user-lecture-week",
			attributeNodes = {
					@NamedAttributeNode(value = "subjectInfo", subgraph = "week-detail")
			},
			subgraphs = { @NamedSubgraph(
					name = "week-detail",
					attributeNodes = { @NamedAttributeNode("lectureList") } 
				)
			}
	)
})
@Data
@Entity
public class UserSubject {
	@Id
	int usId; // 시퀸스 사용할 것
	
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
	
	public String getStudentNumber() {
		return this.userInfo.getStudentNumber();
	}
	public String getSubjectId() {
		return this.subjectInfo.getSubjectId();
	}
	
}
