package com.esummary.elearning.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserData {
	private String studentNumber;
	private String userName;
	private Map<String, String> initialCookies;
}
