package kr.co.smh.module.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.config.security.JwtTokenUtil;
import kr.co.smh.config.security.service.TokenProvider;
import kr.co.smh.module.auth.dao.AuthDAO;
import kr.co.smh.module.auth.model.AuthVO;
import lombok.RequiredArgsConstructor;

@PropertySource("classpath:jwt.yml")
@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthDAO authDAO;
	private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private long expireTimeMs = 1000*60*60;
    @Value("${secret-key}")
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
    public HttpEntity<?> signIn(AuthVO authVO) {
    	AuthVO vo = authDAO.findByAccount(authVO.getEmail());
    	System.out.println(vo.getRole());
    	System.out.println(vo.getEmail());
    	//String token = tokenProvider.accessToken(String.format("%s:%s", vo.getEmail(), vo.getRole()));
    	System.out.println(secretkey);
    	String token = JwtTokenUtil.createToken(vo.getEmail(), expireTimeMs, secretkey);
    	String name = JwtTokenUtil.getUserName(token, secretkey);
    	System.out.println("이름:" + name);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .data(token)
					  .message("로그인 완료.")
					  .build(),
					  HttpStatus.OK);  
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
