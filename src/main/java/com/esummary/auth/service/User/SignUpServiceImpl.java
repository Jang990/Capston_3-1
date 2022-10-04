package com.esummary.auth.service.User;

import java.util.Optional; 

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.esummary.auth.dto.LoginDTO;
import com.esummary.auth.dto.SignUpUserDTO;
import com.esummary.auth.entity.Authority;
import com.esummary.auth.exception.DuplicateMemberException;
import com.esummary.auth.exception.DuplicateNicknameException;
import com.esummary.auth.exception.DeniedElearningCookieException;
import com.esummary.auth.service.ElearningLoginService;
import com.esummary.elearning.exdto.user.UserData;
import com.esummary.entity.user.UserInfo;
import com.esummary.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService{
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final ElearningLoginService elearningLoginService;
	
	//예전 메서드
	@Override
	public boolean exRegisterUser(UserData user) {
		UserInfo userInfo = UserInfo.builder()
				.studentNumber(user.getStudentNumber())
				.nickname(user.getUserName())
				.build();
		userRepository.save(userInfo); 
		return true;
	}
	
	//회원가입 로직
	@Override
    @Transactional
    public SignUpUserDTO signup(SignUpUserDTO signupUserDto) {
    	checkIdDuplicate(signupUserDto.getStudentId()); // 사용자 ID 중복 확인
    	checkNicknameDuplicate(signupUserDto.getNickname()); // 사용자 닉네임 중복 확인
    	
    	// 회원가입을 하기 위해서는 이러닝 계정이 꼭 있어야한다. 꼭!
    	checkElearningLoginCookie(signupUserDto.getStudentId(), signupUserDto.getPassword());
        
        UserInfo user = UserInfo.builder()
                .studentNumber(signupUserDto.getStudentId())
                .password(passwordEncoder.encode(signupUserDto.getPassword()))
                .nickname(signupUserDto.getNickname())
                .roles(Authority.USER)
                .build();

        return SignUpUserDTO.from(userRepository.save(user));
    }
	
	@Override
	public void checkIdDuplicate(String id) {
		if(userRepository.existsByStudentNumber(id))
			throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
		
	}
	
	@Override
	public void checkNicknameDuplicate(String nickname) {
		if(userRepository.existsByNickname(nickname))
			throw new DuplicateNicknameException("닉네임 중복");
	}
	
	
	@Override
	public void checkElearningLoginCookie(String studentId, String password) {
		if(elearningLoginService.getLoginCookies(new LoginDTO(studentId,password)) == null) {
			throw new DeniedElearningCookieException("이러닝에 로그인할 수 없습니다.");
		}
	}
}
