package com.esummary.elearning.entity.user;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.esummary.elearning.dto.UserData;
import com.esummary.elearning.entity.subject.SubjectInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
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
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
	@Id
	private String studentNumber;
	private String userName;
	
	@OneToMany(mappedBy = "userInfo", fetch = FetchType.LAZY, targetEntity = UserSubject.class)
	private List<UserSubject> userSubjects;
	
	@Transient
	private Map<String, String> initialCookies;

//	@OneToMany(mappedBy = "subjectInfo", targetEntity = UserSubject.class)
	@Transient
	private List<SubjectInfo> subjectList;

	public UserInfo(String studentNumber, String userName, Map<String, String> initialCookies) {
		this.studentNumber = studentNumber;
		this.userName = userName;
		this.initialCookies = initialCookies;
	}
	
	public UserInfo(UserData userDTO) {
		this.studentNumber = userDTO.getStudentNumber();
		this.userName = userDTO.getUserName();
		this.initialCookies = userDTO.getInitialCookies();
	}
	
	@Override
	public String toString() {
		return "UserInfo [studentNumber=" + studentNumber + ", userName=" + userName + ", userSubjects=" + userSubjects
				+ ", initialCookies=" + initialCookies + ", subjectList=" + subjectList + "]";
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
