package kr.co.smh.module.auth.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AuthVO {
	private int userId; // 식별번호
	private String email; // 이메일
	private String password; // 비밀번호
	private String koreaName; // 한국이름
	private String foreignerName; // 외국이름
	private String birthDate; // 생년월일
	private String gender; // 성별
	private String foreignerStatus; // 내,외국인 판별 
	private String phoneNum; // 휴대폰 번호
	private String mobileCarrier; // 휴대폰 통신사
	private String privateInfoTerms; // 개인정보동의여부
	private String uniqueIdentifyTerms; // 고유식별정보동의여부
	private String mobileCarrierTerms; // 통신사동의여부
	private String role; // 권한
	private String createAt; // 가입시간
	private String updateAt; // 수정시간
	private String refreshToken;
	private boolean activated;
	//private String authorities;
	private String authorities;
	//private Set<Authority> authorities;
    //refreshToken 갱신
    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public Set<Authority> parseAuthorities() {
        Set<Authority> authoritySet = new HashSet<>();
        if (authorities != null && !authorities.isEmpty()) {
            String[] authorityArray = authorities.split(",");
            for (String authority : authorityArray) {
                authoritySet.add(new Authority(authority.trim()));
            }
        }
        return authoritySet;
    }
    

}
