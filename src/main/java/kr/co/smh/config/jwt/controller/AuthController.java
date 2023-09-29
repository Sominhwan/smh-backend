package kr.co.smh.config.jwt.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.config.jwt.dto.LoginDTO;
import kr.co.smh.config.jwt.dto.TokenDTO;
import kr.co.smh.config.jwt.model.User;
import kr.co.smh.config.jwt.service.UserService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/auth")
public class AuthController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final UserService userService;
	
	// 회원가입
	@PostMapping(value="/join")
	public HttpEntity<?> authJoin(@Nullable @RequestBody User user) {
		log.info("authJoin --> " + user);	
		return userService.signUp(user);
	}
	// 로그인
	@PostMapping(value="/login")
	public ResponseEntity<TokenDTO> authLogin(@Nullable @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
		log.info("authLogin --> " + loginDTO);
		return userService.authLogin(loginDTO, response);
	}
	// 로그아웃
	@GetMapping(value="/logout")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> authLogout(HttpServletResponse response) {
		return ResponseEntity.ok(userService.authLogout(response));
		
	}
	// 유저 정보
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }
    // 관리자 정보
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}
