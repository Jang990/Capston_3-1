package com.esummary.elearning.entity.subject;

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
import javax.persistence.Transient;

import com.esummary.elearning.entity.user.UserInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@NamedEntityGraphs({
	@NamedEntityGraph(
			name = "all-detail-data",
			attributeNodes = {
					@NamedAttributeNode(value = "lectureList"),
					@NamedAttributeNode(value = "noticeList"),
					@NamedAttributeNode(value = "taskList"),
			},
			subgraphs = { @NamedSubgraph(
					name = "lecture-detail",
					attributeNodes = { @NamedAttributeNode("lectures") } 
				)
			}
	),
})
@Entity
@Data
@NoArgsConstructor
public class SubjectInfo {
	@Id
	private String subjectId;				//202214001LLA117		202214043C5019
	private String subjectName;				//[1학년L반] 발명과특허		[3학년C반] 웹보안
	private String subjectOwnerName;		//김영준					용승림
	private String openType;				//normal				normal
	
	@OneToMany(mappedBy = "subjectInfo")
	private List<WeekInfo> lectureList; //1주차, 2주차, 3주차에 관한 정보
	@OneToMany(mappedBy = "subjectInfo")
	private List<TaskInfo> taskList; //과제에 관한 정보
	@OneToMany(mappedBy = "subjectInfo")
	private List<NoticeInfo> noticeList; //공지사항에 관한 정보
	
	public SubjectInfo(String subjectId, String subjectName, String subjectOwnerName, String openType) {
		this.subjectId = subjectId;
		this.subjectName = subjectName;
		this.subjectOwnerName = subjectOwnerName;
		this.openType = openType;
	}
	
	
	
//	@OneToMany(mappedBy = "subjectInfo", fetch = FetchType.LAZY)
//	private List<UserInfo> userList;

	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name="STUDENT_NUMBER")
//	private UserInfo userInfo;
	
	/*
	@OneToMany(mappedBy = "SUBJECT_LIST")
	private UserInfo userInfo;
	*/
}
