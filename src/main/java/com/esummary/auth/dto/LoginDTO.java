package com.esummary.auth.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*; 

@ToString
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
	@Size(min = 5, max = 15)
	@NotNull
	private String studentNumber;
	@Size(min = 5, max = 50)
	@NotNull
	private String password;
}
