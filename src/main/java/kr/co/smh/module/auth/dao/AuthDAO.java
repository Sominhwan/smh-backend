package kr.co.smh.module.auth.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.auth.model.AuthVO;

@Mapper
@Repository
public interface AuthDAO {
	// User 테이블 가져오기
    List<AuthVO> getUserList(); 
    // 로그인 후 회원 데이터 찾기
    AuthVO findByAccount(String email); 
    // 회원 가입
    void insertUser(AuthVO authVO); 
    // 회원 정보 가져오기
    AuthVO getUserByEmail(String email); 
    AuthVO getUserById(int id);
    void updateUser(AuthVO userVo); // 회원 정보 수정
    void deleteUser(int id); // 회원 탈퇴
    Optional<AuthVO> findByUserId(String userEmail);
}
