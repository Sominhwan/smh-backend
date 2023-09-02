package kr.co.smh.module.auth.service;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.LoginDTO;
import kr.co.smh.common.dto.ResDTO;
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
    //private final JwtProvider jwtProvider;
    //private final AuthenticationManager authenticationManager;
    
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
    	System.out.println(authVO.getEmail());
        //UsernamePasswordAuthenticationToken authenticationToken =
                //new UsernamePasswordAuthenticationToken(authVO.getEmail(), authVO.getPassword());
        
        //아이디 체크는 Authentication 에 사용자 입력 아이디, 비번을 넣어줘야지 작동
        //Authentication authentication = authenticationManager.authenticate(authenticationToken);
        //log.info(authentication + " 로그인 처리 authentication");
        
        //jwt accessToken & refreshToken 발급
        //String accessToken = jwtProvider.generateToken(authentication, false);
        //String refreshToken = jwtProvider.generateToken(authentication, true);
        
        //회원 DB에 refreshToken 저장 // TODO 이후에 추가
        //memberService.findMemberAndSaveRefreshToken(authentication.getName(), refreshToken);
        LoginDTO loginDTO = LoginDTO.builder()
                .status(HttpStatus.OK.value())
                .message("로그인 성공")
                //.accessToken(accessToken)
                //.expiredAt(LocalDateTime.now().plusSeconds(jwtProvider.getAccessTokenValidMilliSeconds()/1000))
                //.refreshToken(refreshToken)
                //.issuedAt(LocalDateTime.now())
                .build();
        
        return ResponseEntity.ok(loginDTO);  
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
