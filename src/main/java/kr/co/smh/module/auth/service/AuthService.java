package kr.co.smh.module.auth.service;

import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.auth.dao.AuthDAO;
import kr.co.smh.module.auth.model.AuthVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthDAO authDAO;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<AuthVO> getUserList() {
        return authDAO.getUserList();
    }

    public AuthVO getUserById(int id) {
        return authDAO.getUserById(id);
    }

    public AuthVO getUserByEmail(String email) {
        return authDAO.getUserByEmail(email);
    }

    public HttpEntity<?> signup(AuthVO authVO) { // 회원 가입
	    // password는 암호화해서 DB에 저장           
    	authVO.setPassword(passwordEncoder.encode(authVO.getPassword()));
    	authDAO.insertUser(authVO);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("회원가입이 완료되었습니다.")
					  .build(),
					  HttpStatus.OK);   	  
    }

    public void edit(AuthVO authVO) { // 회원 정보 수정
 		// password는 암호화해서 DB에 저장      
    	authVO.setPassword(passwordEncoder.encode(authVO.getPassword()));
        authDAO.updateUser(authVO);
    }

    public void withdraw(int id) { // 회원 탈퇴
    	authDAO.deleteUser(id);
    }

    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder;
    }
}
