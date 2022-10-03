package com.esummary.auth.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.esummary.auth.entity.Authority;
import com.esummary.entity.user.UserInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpUserDTO {
	@NotNull
	@Size(min = 5)
	private String studentId;
	@NotNull
	@Size(min = 5)
	private String password;
	
	@NotNull
	@Size(min = 3, max = 10) // 3글자이상 10글자 미만
	private String nickname;
	
	private String authorityDto;
	
	//toEntity는 패스워드를 암호화해야하기 때문에 없다.
	
	/**
	 * Entity -> DTO
	 * @param user
	 * @return
	 */
	public static SignUpUserDTO from(UserInfo user) {
		if(user == null) return null;

	      return SignUpUserDTO.builder()
	              .studentId(user.getStudentNumber())
	              .nickname(user.getNickname())
	              .authorityDto(user.getRoles().name())
	              .build();
	}
	
}
