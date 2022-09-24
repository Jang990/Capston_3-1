package com.esummary.auth.service.login;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserRepository userRepository;

	// 로그인 시에 DB에서 유저정보 + 권한정보 가져와서 userdetails.User 객체 생성 후 리턴
	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String username) {
		return userRepository.findByStudentNumber(username)
				.map(user -> createUser(user))
				.orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
	}

	private CustomUserDetails createUser(UserInfo user) {
		return new CustomUserDetails(user);
	}
}