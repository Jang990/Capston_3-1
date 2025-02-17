package com.esummary.entity.user;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

import com.esummary.auth.entity.Authority;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.subject.SubjectInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NamedEntityGraphs({
	@NamedEntityGraph(
			name = "user-own-subject-Detail",
			attributeNodes = {
					@NamedAttributeNode(value = "userSubjects", subgraph = "subject-detail")
			},
			subgraphs = { @NamedSubgraph(
					name = "subject-detail",
					attributeNodes = { @NamedAttributeNode("subjectInfo") } 
				)
			}
	)
})
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_info")
public class UserInfo {
	@Id
	private String studentNumber; //=username
	private String password;
	private String nickname;
	@Enumerated(EnumType.STRING)
	private Authority roles;
	
	@CreationTimestamp
	private Date createdDate;
	
	@OneToMany(mappedBy = "userInfo")
	private List<UserSubject> userSubjects;
	

//	@OneToMany(mappedBy = "subjectInfo", targetEntity = UserSubject.class)
	@Transient
	private List<SubjectInfo> subjectList;
	
	@Builder
	protected UserInfo(String studentNumber, String password, String nickname, Authority roles, List<SubjectInfo> subjectList, List<UserSubject> userSubjects) {
		this.studentNumber = studentNumber;
		this.password = password;
		this.nickname = nickname;
		this.roles = roles;
		this.subjectList = subjectList;
		this.userSubjects = userSubjects;
	}
	
	@Override
	public String toString() {
		return "UserInfo [studentNumber=" + studentNumber + ", nickname=" + nickname + ", userSubjects=" + userSubjects
				+ ", subjectList=" + subjectList + "]";
	}
	
//	public List<SubjectInfo> getSubjectList() {
//		List<SubjectInfo> subjectList = new ArrayList<SubjectInfo>();
//		List<UserSubject> userSubjects = this.getUserSubjects();
//		for (UserSubject userSubject : userSubjects) {
//			subjectList.add(userSubject.getSubjectInfo());
//		}
//		return subjectList;
//	}
	
//	@OneToMany(mappedBy = "subjectInfo", fetch = FetchType.LAZY)
//	@Transient
//	private List<UserSubject> us; 
	
}
