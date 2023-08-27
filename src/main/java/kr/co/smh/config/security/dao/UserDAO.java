package kr.co.smh.config.security.dao;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDAO {
    // 폼에 입력받은 아이디(소셜 아이디)를 통해 해당 유저 정보 받아오기
    Optional<User> findByEmail(String email);
}
