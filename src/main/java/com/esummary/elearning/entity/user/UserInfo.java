package com.esummary.elearning.entity.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.jsoup.nodes.Document;

import com.esummary.elearning.entity.subject.SubjectInfo;

import lombok.Data;
@NamedEntityGraphs({
	@NamedEntityGraph(
			name = "user-own-subject",
			attributeNodes = {
					@NamedAttributeNode("userSubjects")
			}
	)
})
@Entity
@Data
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
