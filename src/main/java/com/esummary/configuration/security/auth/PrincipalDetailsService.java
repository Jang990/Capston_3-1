package com.esummary.configuration.security.auth;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.esummary.elearning.entity.user.UserInfo;
import com.esummary.elearning.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<UserInfo> userCheck = userRepository.findByStudentNumber(username);
		if(userCheck.isEmpty()) return null;
		
		return new PrincipalDetails(userCheck.get());
	}

}
