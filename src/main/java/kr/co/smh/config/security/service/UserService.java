package kr.co.smh.config.security.service;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import kr.co.smh.config.security.dao.UserDAO;
import kr.co.smh.config.security.model.UserVO;
import kr.co.smh.module.auth.model.AuthVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserDAO userDAO;

	
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        AuthVO authVO = userDAO.findByEmail(email);
//        if (userInfoEntity == null) {
//            throw new UsernameNotFoundException("잘못된 접근입니다.");
//        }      
//        return new PrincipalDetails(userInfoEntity);     
//    }
//	
}
