package com.esummary.elearning.exdto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class LoginCheck_DTO {
	private String studentNumber;
	private String password;
}
