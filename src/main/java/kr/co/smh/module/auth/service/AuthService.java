package kr.co.smh.module.auth.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.common.dto.TokenDTO;
import kr.co.smh.config.security.JwtFilter;
import kr.co.smh.config.security.JwtProvider;
import kr.co.smh.module.auth.dao.AuthDAO;
import kr.co.smh.module.auth.model.AuthVO;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthDAO authDAO;
	private static final Logger log = LogManager.getLogger("kr.co.smh");
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtProvider jwtProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    @Value("${jwt.secretKey}")
    private String secretkey; 
    public List<AuthVO> getUserList() {
        return authDAO.getUserList();
    }

    public AuthVO getUserById(int id) {
        return authDAO.getUserById(id);
    }

    public AuthVO getUserByEmail(String email) {
        return authDAO.getUserByEmail(email);
    }
    // 로그인
    public ResponseEntity<?> signIn(AuthVO authVO) {
       UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authVO.getEmail(), authVO.getPassword());
       // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
       Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
       // 해당 객체를 SecurityContextHolder에 저장하고
       SecurityContextHolder.getContext().setAuthentication(authentication);
       // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
       String jwt = jwtProvider.createToken(authentication);
       HttpHeaders httpHeaders = new HttpHeaders();
       // response header에 jwt token에 넣어줌
       httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

       // tokenDto를 이용해 response body에도 넣어서 리턴
       return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.OK);
    }
    // 회원 가입
    public HttpEntity<?> signUp(AuthVO authVO) { 
	    // password는 암호화해서 DB에 저장           
    	authVO.setPassword(passwordEncoder.encode(authVO.getPassword()));
    	authDAO.insertUser(authVO);
    	
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("회원가입이 완료되었습니다.")
					  .build(),
					  HttpStatus.OK);   	  
    }

    public void edit(AuthVO authVO) { // 회원 정보 수정
 		// password는 암호화해서 DB에 저장      
    	authVO.setPassword(passwordEncoder.encode(authVO.getPassword()));
        authDAO.updateUser(authVO);
    }

    public void withdraw(int id) { // 회원 탈퇴
    	authDAO.deleteUser(id);
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
