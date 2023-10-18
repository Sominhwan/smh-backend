package kr.co.smh.config.jwt.model;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private int userId; // 식별번호
	private String email; // 이메일
	private String username;
	private String nickname; // 닉네임
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
	private boolean activated; 

    private Set<Authority> authorities;
}