////package kr.co.smh.module.auth.service;
////
////import org.apache.logging.log4j.LogManager;
////import org.apache.logging.log4j.Logger;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.http.HttpEntity;
////import org.springframework.http.HttpStatus;
////import org.springframework.http.ResponseEntity;
////import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
////import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
////import org.springframework.security.core.Authentication;
////import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
////import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import kr.co.smh.common.dto.TokenInfo;
//import kr.co.smh.common.dto.ResDTO;
//import kr.co.smh.config.security.JwtTokenProvider;
//import kr.co.smh.module.auth.dao.AuthDAO;
//import kr.co.smh.module.auth.model.AuthVO;
//import lombok.RequiredArgsConstructor;
//
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//	private final AuthDAO authDAO;
//	private static final Logger log = LogManager.getLogger("kr.co.smh");
//    private final PasswordEncoder encoder;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//    //private final JwtTokenProvider jwtTokenProvider;
//  
//    @Value("${jwt.secretKey}")
//    private String secretkey; 
//  
//    @Transactional
//    public TokenInfo login(AuthVO authVO) {
//        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
//        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authVO.getEmail(), authVO.getPassword());
// 
//        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
//        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
//        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
// 
//        // 3. 인증 정보를 기반으로 JWT 토큰 생성
//        //TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
//        //log.info(tokenInfo);
//        return tokenInfo;
//    }
//    
//    // 회원 가입
//    public HttpEntity<?> signUp(AuthVO authVO) { 
////        Authority authority = Authority.builder()
////                .authorityName("ROLE_USER")
////                .build();    
//        //authVO.setAuthorities(Collections.singleton(authority));
//	    // password는 암호화해서 DB에 저장           
//    	authVO.setPassword(encoder.encode(authVO.getPassword()));
//    	authDAO.insertUser(authVO);
//    	
//		return new ResponseEntity<>(
//				ResDTO.builder()
//					  .code(0)
//					  .message("회원가입이 완료되었습니다.")
//					  .build(),
//					  HttpStatus.OK);   	  
//    }
//}
