package kr.co.smh.config.jwt.dao;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.smh.config.jwt.model.Authority;
import kr.co.smh.config.jwt.model.User;

@Mapper
@Repository
public interface UserDAO {
    // 회원 가입
    void insertUser(User user); 
    // 회원 가입 권한 저장
    void insertUserAuthority(@Param("email") String email, @Param("authorityName") String authorityName);
    // 로그아웃 RefreshToken 삭제
    void deleteRefreshToken(String email);
    // 회원 정보 가져오기(이메일)
    User findOneWithAuthoritiesByUsername(String email);
    // 회원 아이디 가져오기(이름, 휴대폰번호)
    User findUserId(@Param("koreaName") String koreaName, @Param("phoneNum") String phoneNum);
    // 권한 정보 가져오기
    Set<Authority> findOneWithAuthorityName(int userId);
    // refresh token 저장
    void insertRefreshToken(@Param("email") String email, @Param("refreshToken") String refreshToken);
    // refresh token 비교
    String getRefreshToken(String email);
    // 비밀번호 찾기(아이디 검증)
    boolean findId(String email);
    // 비밀번호 변경 
    void changePassword(@Param("password") String password, @Param("email") String email);
}
