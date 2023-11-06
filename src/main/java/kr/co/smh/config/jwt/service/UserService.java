package kr.co.smh.config.jwt.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import kr.co.smh.util.gmail.service.GmailService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final kr.co.smh.config.jwt.service.TokenProvider tokenProvider;
    private final CertificationNumberService certificationNumberService;
    private final GmailService gmailService;
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
    // 아이디 찾기(휴대폰번호 확인, 인증번호 전송)
    @Transactional(readOnly = false)
    public HttpEntity<?> authPhoneNumId(String koreaName, String phoneNum) {
    	Integer code = 0;
    	String data = "";
    	Map<String, Object> jsonData = new HashMap<>();
    	String content = "";
    	User user = userDAO.findUserId(koreaName, phoneNum);
    	String phone = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7);
    	
    	if(user == null) {
    		code = 1;
    		data = "가입된 계정이 없습니다.";
    	} else {
    		// 인증번호 생성
    		int certificationNumber = certificationNumberService.createCertificationNumber(); 
    		content += "[SMS] 본인확인\n인증번호 ["+ certificationNumber + "] 타인에게 절대 알려주지 마세요.";
    		try {
				if(cafe24Service.sendSMS(user.getUserId(), content, phone)) {
					code = 0;
					jsonData.put("email", user.getEmail());
					jsonData.put("certificationNumber", String.valueOf(certificationNumber));
				} else {
					code = 1;
		    		data = "알 수 없는 오류가 발생했습니다.";
				}
			} catch (Exception e) {
	    		code = 1;
	    		data = "알 수 없는 오류가 발생했습니다.";
			}    		
    	}
    	
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(code)
					  .data(data)
					  .data2(jsonData)
					  .build(),
					  HttpStatus.OK);      	
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
    @Transactional(readOnly = false)
    public HttpEntity<?> authPhoneNum(String email, String koreaName, String phoneNum) {
    	Integer code = 0;
    	String data = "";
    	String content = "";
    	User user = userDAO.findOneWithAuthoritiesByUsername(email);
    	String phone = phoneNum.substring(0, 3) + "-" + phoneNum.substring(3, 7) + "-" + phoneNum.substring(7);
 
    	if(!koreaName.equals(user.getKoreaName()) || !phoneNum.equals(user.getPhoneNum())) {
    		code = 1;
    		data = "이름 또는 휴대폰 번호가 가입된 아이디 정보와 일치하지 않습니다.";
    	} else {
    		// 인증번호 생성
    		int certificationNumber = certificationNumberService.createCertificationNumber(); 
    		content += "[SMS] 본인확인\n인증번호 ["+ certificationNumber + "] 타인에게 절대 알려주지 마세요.";
    		try {
				if(cafe24Service.sendSMS(user.getUserId(), content, phone)) {
					code = 0;
					data = String.valueOf(certificationNumber);
				} else {
					code = 1;
		    		data = "알 수 없는 오류가 발생했습니다.";
				}
			} catch (Exception e) {
	    		code = 1;
	    		data = "알 수 없는 오류가 발생했습니다.";
			}    		
    	}
	
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(code)
					  .data(data)
					  .build(),
					  HttpStatus.OK);      	
    }
    // 비밀번호 변경
    public HttpEntity<?> authPassword(String email, String password) {
    	User user = userDAO.findOneWithAuthoritiesByUsername(email);
    	boolean flag = passwordEncoder.matches(password, user.getPassword());
    	if(flag) {
    		return new ResponseEntity<>(
    				ResDTO.builder()
    					  .code(1)
    					  .data("이전과 같은 비밀번호입니다. 다른 비밀번호를 입력하세요.")
    					  .build(),
    					  HttpStatus.OK); 
    	} else {
    		String changePassword = passwordEncoder.encode(password);
    		userDAO.changePassword(changePassword, email);
    		return new ResponseEntity<>(
    				ResDTO.builder()
    					  .code(0)
    					  .data("비밀번호 변경에 성공하였습니다.")
    					  .build(),
    					  HttpStatus.OK); 
    	}	
    }
    // 비밀번호 찾기(이메일 인증)
    public HttpEntity<?> authEmail(String email, String name, String birthDate) {
       	User user = userDAO.findOneWithAuthoritiesByUsername(email);
       	if(user.getKoreaName().equals(name) && user.getBirthDate().equals(birthDate)) {
       		int certificationNumber = certificationNumberService.createCertificationNumber();
			gmailService.sendEmail(user.getUserId(), email, name, certificationNumber);

    		return new ResponseEntity<>(
    				ResDTO.builder()
    					  .code(0)
    					  .data(String.valueOf(certificationNumber))
    					  .build(),
    					  HttpStatus.OK); 
       	} else {
    		return new ResponseEntity<>(
    				ResDTO.builder()
    					  .code(1)
    					  .data("회원정보가 일치하지 않습니다.\n다시 입력해주세요.")
    					  .build(),
    					  HttpStatus.OK); 
       	}
    }
    // 닉네임 중복확인
    public HttpEntity<?> authNickname(String nickname) {
    	boolean flag = userDAO.checkNickname(nickname);
    	if(flag) { // 중복된 닉네임인 경우
    		return new ResponseEntity<>(
    				ResDTO.builder()
    					  .code(0)
    					  .data("이미 존재하는 닉네임입니다.")
    					  .build(),
    					  HttpStatus.OK);
    	} else { // 중복되지 않은 경우
    		return new ResponseEntity<>(
    				ResDTO.builder()
    					  .code(1)
    					  .data("사용가능한 닉네임입니다.")
    					  .build(),
    					  HttpStatus.OK);
    	}
    }
}