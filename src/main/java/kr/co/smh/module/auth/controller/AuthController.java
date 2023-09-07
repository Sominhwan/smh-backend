package kr.co.smh.module.auth.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.common.dto.TokenInfo;
import kr.co.smh.module.auth.model.AuthVO;
import kr.co.smh.module.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/auth")
public class AuthController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final AuthService authService;
	
	// 회원가입
	@PostMapping(value="/join")
	public HttpEntity<?> authJoin(@Nullable @RequestBody AuthVO vo) {
		log.info("authJoin --> " + vo);	
		return authService.signUp(vo);
	}
	// 로그인
	@PostMapping(value="/login")
	public ResponseEntity<TokenInfo> authLogin(@Nullable @RequestBody AuthVO vo) {
		log.info("authLogin --> " + vo);	
	    TokenInfo token = authService.login(vo);
	    return ResponseEntity.ok(token);
	}
}
