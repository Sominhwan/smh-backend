package kr.co.smh.config.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.smh.module.auth.dao.AuthDAO;
import kr.co.smh.module.auth.model.AuthVO;
import kr.co.smh.module.auth.model.AuthorityVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
 
    private final AuthDAO authDAO;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityVO authorityVO;

 
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authDAO.findByUserId(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }
 
    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
	private UserDetails createUserDetails(AuthVO authVO) {
		System.out.println(authVO.getEmail());
		System.out.println(authVO.getPassword());
    	System.out.println("zz:" + authVO.getAuthorities());

		authorityVO.setAuthority(authVO.parseAuthorities());

        List<GrantedAuthority> grantedAuthorities = authorityVO.getAuthority().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return User.builder()
                .username(authVO.getEmail())
                .password(passwordEncoder.encode(authVO.getPassword()))
                .roles(authVO.getAuthorities())
                .build();
    }
}