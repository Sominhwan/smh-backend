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
import kr.co.smh.common.service.CertificationNumberService;
import kr.co.smh.config.jwt.dao.UserDAO;
import kr.co.smh.config.jwt.dto.LoginDTO;
import kr.co.smh.config.jwt.dto.TokenDTO;
import kr.co.smh.config.jwt.model.Authority;
import kr.co.smh.config.jwt.model.User;
import kr.co.smh.config.jwt.util.SecurityUtil;
import kr.co.smh.util.cafe24.service.Cafe24Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final kr.co.smh.config.jwt.service.TokenProvider tokenProvider;
    private final CertificationNumberService certificationNumberService;
    private final Cafe24Service cafe24Service;
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
        SecurityContextHolder.getContext().setAuthentication(authentication);
	    log.info("자동 로그인 여부 -->" + loginDTO.isAutoLogin());
	    String refreshToken = "";
	    if(loginDTO.isAutoLogin()) {
	        Cookie cookie = tokenProvider.createCookie(authentication);
	        refreshToken = cookie.getValue();
	        response.addCookie(cookie);
	        userDAO.insertRefreshToken(loginDTO.getEmail(), refreshToken);
	        log.info("리플레쉬 토큰 --> " + refreshToken);
	    }

        String accessToken = tokenProvider.createAccessToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);
   
	    return new ResponseEntity<>(new TokenDTO(accessToken, refreshToken), httpHeaders, HttpStatus.OK);    	
    }
    // 로그아웃
    @Transactional(readOnly = false)
    public ResponseEntity<?> authLogout(HttpServletResponse response) {
    	String username = SecurityUtil.getCurrentUsername();
    	userDAO.deleteRefreshToken(username);
    	Cookie cookie = new Cookie("refreshtoken", null);
    	cookie.setMaxAge(0);
    	cookie.setPath("/");
    	response.addCookie(cookie);
    	return new ResponseEntity<>(HttpStatus.OK);
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
    // 비밀번호 찾기(아이디 검증)
    @Transactional(readOnly = true)
    public HttpEntity<?> authId(String email) {
    	boolean flag = userDAO.findId(email);
    	
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .data(flag)
					  .build(),
					  HttpStatus.OK);      	
    }
    // 비밀번호 찾기(휴대폰번호 확인, 인증번호 전송)
    @Transactional(readOnly = true)
    public HttpEntity<?> authPhoneNum(String email, String koreaName, String phoneNum) {
    	Integer code = 0;
    	String data = "";
    	String content = "";
    	User user = userDAO.findOneWithAuthoritiesByUsername(email);

 
    	if(!koreaName.equals(user.getKoreaName()) || !phoneNum.equals(user.getPhoneNum())) {
    		code = 1;
    		data = "이름 또는 휴대폰 번호가 가입된 아이디 정보와 일치하지 않습니다.";
    	} else {
    		// 인증번호 생성
    		int certificationNumber = certificationNumberService.createCertificationNumber(); 
    		content += "인증번호 ["+ certificationNumber + "] 타인에게 절대 알려주지 마세요.";
    		try {
				cafe24Service.sendSMS(content, phoneNum);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}    		// TODO 
    	}
	
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(code)
					  .data(data)
					  .build(),
					  HttpStatus.OK);      	
    }
}