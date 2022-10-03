package com.esummary.user.dto;

import java.sql.Date;

import lombok.*;

/**
 * api/users에서 사용되는 DTO 
 * 기본적인 사용자 정보를 가져올 때 사용
 */
@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	String studentId;
	String nickname;
	Date createdDate;
}
