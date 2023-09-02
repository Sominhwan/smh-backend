package kr.co.smh.config.security.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smh.common.dto.LoginDTO;
import kr.co.smh.common.dto.RefreshTokenDTO;
import kr.co.smh.config.security.JwtProvider;
import kr.co.smh.config.security.model.UserRole;
import kr.co.smh.exception.RefreshTokenGrantTypeException;
import kr.co.smh.module.auth.dao.AuthDAO;
import kr.co.smh.module.auth.model.AuthVO;
import lombok.RequiredArgsConstructor;

@Configuration
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final AuthDAO authDAO;
    private final JwtProvider jwtProvider;
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	/**
	 * 로그인 요청 회원 찾기
	 * @param username 요청 아이디
	 * @return 회원 정보 넣은 security User 객체
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String username){
	    log.info("로그인 요청 회원 찾기");
	    AuthVO authVO = authDAO.findByUserId(username)
	            .orElseThrow(() -> new UsernameNotFoundException(username + " 아이디가 일치하지 않습니다"));
	    
	    return new User(authVO.getEmail(), authVO.getPassword(), authorities(Set.of(UserRole.USER, UserRole.ADMIN)));
	}
	
	private Collection<? extends GrantedAuthority> authorities(Set<UserRole> roles) {
	    return roles.stream()
	    		.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
	    		.collect(Collectors.toList());

	}
	
	
    /**
     * 회원에게 refreshToken 저장
     * @param username 요청 아이디
     * @param refreshToken refreshToken 값
     */
    @Transactional
    public void findMemberAndSaveRefreshToken(String username, String refreshToken) {
        AuthVO authVO = authDAO.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " 아이디가 일치하지 않습니다"));
        authVO.updateRefreshToken(refreshToken);
    }

    /**
     * refreshToken 으로 accessToken 재발급
     * @param refreshTokenDto accessToken 재발급 요청 dto
     * @return json response
     */
    @Transactional
    public LoginDTO refreshToken(RefreshTokenDTO refreshTokenDTO) {
        if (!refreshTokenDTO.getGrantType().equals("refreshToken"))
            throw new RefreshTokenGrantTypeException("올바른 grantType 을 입력해주세요");

        Authentication authentication = jwtProvider.getAuthentication(refreshTokenDTO.getRefreshToken());
        AuthVO authVO;
//        AuthVO authVO = authDAO.findMemberByUsernameAndRefreshToken(authentication.getName(), refreshTokenDto.getRefreshToken())
//                .orElseThrow(() -> new InvalidRefreshTokenException("유효하지 않은 리프레시 토큰입니다"));
        //TODO InvalidRefreshTokenException 예외 Handler

        //jwt accessToken & refreshToken 발급
        String accessToken = jwtProvider.generateToken(authentication, false);
        String refreshToken = jwtProvider.generateToken(authentication, true);

        //refreshToken 저장 (refreshToken 은 한번 사용후 폐기)
        //authVO.updateRefreshToken(refreshToken);

        LoginDTO response = LoginDTO.builder()
                .status(HttpStatus.OK.value())
                .message("accessToken 재발급 성공")
                .accessToken(accessToken)
                .expiredAt(LocalDateTime.now().plusSeconds(jwtProvider.getAccessTokenValidMilliSeconds()/1000))
                .refreshToken(refreshToken)
                .issuedAt(LocalDateTime.now())
                .build();
        return response;
    }	
}