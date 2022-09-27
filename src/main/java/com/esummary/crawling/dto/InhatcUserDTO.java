package com.esummary.crawling.dto;

import java.util.Map;

import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.user.UserInfo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InhatcUserDTO {
	private String studentId;
	private Map<String, String> initialCookies;
	
	public static UserInfo toEntity(InhatcUserDTO userData) {
		return UserInfo.builder()
				.studentNumber(userData.getStudentId())
				.build();
	}
}
