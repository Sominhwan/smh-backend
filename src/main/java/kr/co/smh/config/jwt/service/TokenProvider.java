package kr.co.smh.config.jwt.service;

import java.net.URLEncoder;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import kr.co.smh.config.jwt.dao.UserDAO;
import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class TokenProvider implements InitializingBean {
   private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
   private static final String AUTHORITIES_KEY = "auth";
   private final String secret;
   private final long accessTokenValidityInMilliseconds;
   private final long refreshTokenValidityInMilliseconds;
   private Key key;
   private UserDAO userDAO;
   private CustomUserDetailsService customUserDetailsService;

   public TokenProvider(@Value("${jwt.secretKey}") String secret, @Value("${jwt.accessToken-valid-seconds}") long accessTokenValidityInSeconds, 
		   @Value("${jwt.refreshToken-valid-seconds}") long refreshTokenValidityInSeconds, UserDAO userDAO, CustomUserDetailsService customUserDetailsService) {
      this.secret = secret;
      this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 3000;
      this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 30;
      this.userDAO = userDAO;
      this.customUserDetailsService = customUserDetailsService;
   }

   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);
   }
   // Access Token 발급
   public String createAccessToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));
      long now = (new Date()).getTime();
      
      Date validity = new Date(now + this.accessTokenValidityInMilliseconds);
      Date date = new Date();
      return Jwts.builder()
         .setSubject(authentication.getName())
         .claim(AUTHORITIES_KEY, authorities)
         //.setIssuedAt(date)
         .signWith(key, SignatureAlgorithm.HS512)
         .setExpiration(validity)
         .compact();
   }
   // Refresh Token 발급
   public String createRefreshToken(Authentication authentication) {
       long now = (new Date()).getTime();
       Date validity = new Date(now + this.refreshTokenValidityInMilliseconds);
       Date date = new Date();
       return Jwts.builder()
               .setSubject(authentication.getName())
               .setExpiration(validity)
               //.setIssuedAt(date)
               .signWith(key, SignatureAlgorithm.HS512)
               .compact();
   }  
   // Refresh Token Cookie 생성
   public Cookie createCookie(Authentication authentication) {
       String cookieName = "refreshtoken";
       String cookieValue = createRefreshToken(authentication);
       var RTcookie = URLEncoder.encode(cookieValue, UTF_8);
       Cookie cookie = new Cookie(cookieName, RTcookie);
       cookie.setHttpOnly(true);
       cookie.setSecure(true);
       cookie.setPath("/");
       cookie.setMaxAge(24 * 60 * 60);
       return cookie;
   }
   public Authentication getAuthentication(String token) {
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      User principal = new User(claims.getSubject(), "", authorities);

      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }
   // 토큰 검증
   public boolean validateToken(String token) {
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         return true;
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         logger.info("잘못된 JWT 서명입니다.");
      } catch (ExpiredJwtException e) {
         logger.info("만료된 JWT 토큰입니다.");
      } catch (UnsupportedJwtException e) {
         logger.info("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         logger.info("JWT 토큰이 잘못되었습니다.");
      }
      return false;
   }
   public Claims getUserInfoFromToken(String token) {
       return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
   }
   // DB에서 refresh token 가져옴
   public boolean getRefreshTokenIsTrue(String email, String refreshToken) {
       return userDAO.getRefreshToken(email).equals(refreshToken);
   }
   // 유저 정보
   public kr.co.smh.config.jwt.model.User getUserDetail(String email){
	   return userDAO.findOneWithAuthoritiesByUsername(email);
   }
   public Authentication createAuthentication(String email) {
       UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
       logger.info("권한 -->" + userDetails.getAuthorities());
       return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
   }
}