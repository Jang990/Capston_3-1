package com.esummary.elearning.exdto.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.esummary.elearning.entity.user.UserInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserToRegister {
	@NotNull
	private String studentId;
	@NotNull
	private String password;
	
	@NotNull
	@Size(min = 3, max = 20) // 3글자이상 20글자 미만
	private String nickname;
	
	public static UserInfo toEntity(UserToRegister user) {
		return UserInfo.builder()
				.studentNumber(user.getStudentId())
				.password(user.getPassword())
				.nickname(user.getNickname())
				.roles("USER")
				.build();
	}
	
}
