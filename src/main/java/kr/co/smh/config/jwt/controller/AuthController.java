package kr.co.smh.config.jwt.controller;


import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.config.jwt.dto.LoginDTO;
import kr.co.smh.config.jwt.dto.TokenDTO;
import kr.co.smh.config.jwt.model.User;
import kr.co.smh.config.jwt.service.JwtFilter;
import kr.co.smh.config.jwt.service.UserService;
import lombok.RequiredArgsConstructor;



@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/auth")
public class AuthController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final UserService userService;
    private final kr.co.smh.config.jwt.service.TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
	
	// 회원가입
	@PostMapping(value="/join")
	public HttpEntity<?> authJoin(@Nullable @RequestBody User user) {
		log.info("authJoin --> " + user);	
		return userService.signUp(user);
	}
	// 로그인
	@PostMapping(value="/login")
	public ResponseEntity<TokenDTO> authLogin(@Nullable @RequestBody LoginDTO loginDTO) {
		log.info("authLogin --> " + loginDTO);
	    UsernamePasswordAuthenticationToken authenticationToken =
	          new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
	
	    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        String jwt = tokenProvider.createToken(authentication);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        

	    return new ResponseEntity<>(new TokenDTO(jwt), httpHeaders, HttpStatus.OK);
	}
	
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<User> getMyUserInfo(HttpServletRequest request) {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }
}
