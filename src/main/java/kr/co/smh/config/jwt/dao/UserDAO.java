package kr.co.smh.config.jwt.dao;

import java.util.Optional;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.smh.config.jwt.model.Authority;
import kr.co.smh.config.jwt.model.User;

@Mapper
@Repository
public interface UserDAO {
	// User 테이블 가져오기
    //List<AuthVO> getUserList(); 
    // 로그인 후 회원 데이터 찾기
   // AuthVO findByAccount(String email); 
    // 회원 가입
    void insertUser(User user); 
    void insertUserAuthority(@Param("email") String email, @Param("authorityName") String authorityName);
    // 회원 정보 가져오기
    //AuthVO getUserByEmail(String email); 
   // AuthVO getUserById(int id);
    //void updateUser(AuthVO userVo); // 회원 정보 수정
    //void deleteUser(int id); // 회원 탈퇴
    User findOneWithAuthoritiesByUsername(String email);
    Set<Authority> findOneWithAuthorityName(int userId);
}
