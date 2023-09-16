package kr.co.smh.config.jwt.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.co.smh.config.jwt.dao.UserDAO;
import kr.co.smh.config.jwt.model.User;
import lombok.RequiredArgsConstructor;

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
   private final UserDAO userDAO;
	private static final Logger log = LogManager.getLogger("kr.co.smh");


   @Override
   @Transactional
   public UserDetails loadUserByUsername(final String username) {   
	   User user =  userDAO.findOneWithAuthoritiesByUsername(username);
	   user.setAuthorities(userDAO.findOneWithAuthorityName(user.getUserId()));
   	
       return createUser(username, user);
   }

   private org.springframework.security.core.userdetails.User createUser(String username, User user) {
//      TODO 이후에 회원 활성, 비활성 여부 추가 시 검사
//      if (!user.isActivated()) {
//         throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
//      }

      List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
              .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
              .collect(Collectors.toList());
      log.info(grantedAuthorities);
      return new org.springframework.security.core.userdetails.User(user.getUsername(),
              user.getPassword(),
              grantedAuthorities);
   }
}