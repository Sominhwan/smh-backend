package kr.co.smh.module.auth.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.auth.model.AuthVO;

@Mapper
@Repository
public interface AuthDAO {
    List<AuthVO> getUserList(); // User 테이블 가져오기
    void insertUser(AuthVO authVO); // 회원 가입
    AuthVO getUserByEmail(String email); // 회원 정보 가져오기
    AuthVO getUserById(int id);
    void updateUser(AuthVO userVo); // 회원 정보 수정
    void deleteUser(int id); // 회원 탈퇴
}
