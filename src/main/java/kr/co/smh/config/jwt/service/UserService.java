package kr.co.smh.config.jwt.service;

import java.util.Collections;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.config.jwt.dao.UserDAO;
import kr.co.smh.config.jwt.dto.LoginDTO;
import kr.co.smh.config.jwt.dto.TokenDTO;
import kr.co.smh.config.jwt.model.Authority;
import kr.co.smh.config.jwt.model.User;
import kr.co.smh.config.jwt.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final kr.co.smh.config.jwt.service.TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;

	// 회원가입
    @Transactional
    public HttpEntity<?> signUp(User user) { 
    	// 중복가입 체크
	    if (userDAO.findOneWithAuthoritiesByUsername(user.getEmail()) != null) {
			return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("이미 존재하는 회원입니다..")
						  .build(),
						  HttpStatus.BAD_REQUEST); 
	    }    	
	    
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
    // 로그인
    public ResponseEntity<TokenDTO> authLogin(LoginDTO loginDTO, HttpServletResponse response) {
	    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
	    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //SecurityContextHolder.getContext().setAuthentication(authentication);
	    if(loginDTO.isAutoLogin()) {
	        Cookie cookie = tokenProvider.createCookie(authentication);
	        String refreshToken = cookie.getValue();
	        response.addCookie(cookie);
	        userDAO.insertRefreshToken(loginDTO.getEmail(), refreshToken);
	        log.info("리플레쉬 토큰 --> " + refreshToken);
	    }

        String acessToken = tokenProvider.createAccessToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + acessToken);
   
	    return new ResponseEntity<>(new TokenDTO(acessToken), httpHeaders, HttpStatus.OK);    	
    }
    // 유저 정보
    @Transactional(readOnly = true)
    public User getMyUserWithAuthorities() {
    	 String username = SecurityUtil.getCurrentUsername();
        return userDAO.findOneWithAuthoritiesByUsername(username);
    }    
    // 관리자 정보
    @Transactional(readOnly = true)
    public User getUserWithAuthorities(String username) {
    	User user = userDAO.findOneWithAuthoritiesByUsername(username);
    	user.setAuthorities(userDAO.findOneWithAuthorityName(user.getUserId()));
    	log.info("User Authority -->" + user.getAuthorities());
        return user;
    }
}