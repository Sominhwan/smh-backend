package kr.co.smh.config.jwt.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.config.jwt.dao.UserDAO;
import kr.co.smh.config.jwt.model.Authority;
import kr.co.smh.config.jwt.model.User;
import kr.co.smh.config.jwt.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;
	private static final Logger log = LogManager.getLogger("kr.co.smh");

    @Transactional
    public HttpEntity<?> signUp(User user) { 
    	// TODO 중복가입 체크
//    if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
//        throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
//    }    	
    
    Authority authority = Authority.builder()
    		.authorityName("ROLE_USER")
            .build();        
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setAuthorities(Collections.singleton(authority));
    user.setActivated(true);
    log.info(authority.getAuthorityName());
    userDAO.insertUser(user);
    userDAO.insertUserAuthority(user.getEmail(), authority.getAuthorityName());
  	
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("회원가입이 완료되었습니다.")
					  .build(),
					  HttpStatus.OK);   	  
  }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(String username) {
    	User user = userDAO.findOneWithAuthoritiesByUsername(username);
    	log.info(user.getUserId());
    	//Set<Authority> authority = userDAO.findOneWithAuthorityName(user.getUserId());
    	user.setAuthorities(userDAO.findOneWithAuthorityName(user.getUserId()));
    	log.info("User Authority -->" + user.getAuthorities());
        return user;
    }

    @Transactional(readOnly = true)
    public User getMyUserWithAuthorities() {
    	 String username = SecurityUtil.getCurrentUsername();
        return userDAO.findOneWithAuthoritiesByUsername(username);
    }
}