package kr.co.smh.config.security.model;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.smh.module.auth.model.AuthVO;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserVO implements UserDetails{
	private AuthVO authVO;
	// 일반 로그인시 사용하는 생성자
	public UserVO(AuthVO authVO) {
		this.authVO = authVO;
	}
	// 유저 권한 리턴
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> role = new ArrayList<>();	
		role.add(new GrantedAuthority() {		
			@Override
			public String getAuthority() {
				return authVO.getRole();
			}
		});
		return role;
	}
	// 유저 비밀번호
	@Override
	public String getPassword() {
		return null;
	}
	// 유저 id
	@Override
	public String getUsername() {
		return null;
	}
	// 계정 만료 여부
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}
	// 계정장금
	@Override
	public boolean isAccountNonLocked() {
		return false;
	}
	// 패스워드 만료 여부 
	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}
	// 계정 사용 가능 여부
	@Override
	public boolean isEnabled() {
		return false;
	}

}
