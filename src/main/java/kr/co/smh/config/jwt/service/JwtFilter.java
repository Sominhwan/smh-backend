package kr.co.smh.config.jwt.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import kr.co.smh.config.jwt.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter  {
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
	public static final String AUTHORIZATION_HEADER = "Authorization";
	private final TokenProvider tokenProvider;
   
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	      String jwt = resolveToken(request);
	      logger.info("accessToken 검증 -->" + jwt);
	      String requestURI = request.getRequestURI();

	      if (StringUtils.hasText(jwt)) {
	    	  if(!tokenProvider.validateToken(jwt)) { // access token 만료시
	    		  logger.info("accessToken 기간 만료 -->" + jwt);
		    	  Cookie rc[] = request.getCookies();
		    	  String refreshToken = "";
		    	  for(Cookie cookie : rc) {
		    		  logger.info(cookie.getName());
		    		  if (cookie.getName().equals("refreshtoken")) {
		    			  refreshToken = cookie.getValue();
		    			  logger.info("찾은 refreshToken -->" + refreshToken);
		    		  }
		    	  }
		    	  Claims AccessTokenInfo = AuthenticatedUserByToken(refreshToken);
		    	  if(tokenProvider.getRefreshTokenIsTrue(AccessTokenInfo.getSubject(), refreshToken)) {
		    		  User user = tokenProvider.getUserDetail(AccessTokenInfo.getSubject());

		    		  Authentication authentication = tokenProvider.createAuthentication(user.getUsername());

		    		  SecurityContextHolder.getContext().setAuthentication(authentication);
		    	      String accessToken = tokenProvider.createAccessToken(authentication);
		    	      logger.info("새로 발급받은 accessToken -->" + accessToken);
		    		  response.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);
		    	  }

	    	  } else {
		    	  Authentication authentication = tokenProvider.getAuthentication(jwt);
		    	  SecurityContextHolder.getContext().setAuthentication(authentication);
		    	  logger.info("인증완료");
	    	  }
	      } else {
	    	  logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
	      }
	      filterChain.doFilter(request, response);
	}

   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }
      return null;	
   }
   public void setAuthentication(String username) {
       SecurityContext context = SecurityContextHolder.createEmptyContext();
       Authentication authentication = tokenProvider.createAuthentication(username);
       context.setAuthentication(authentication);
       SecurityContextHolder.setContext(context);
   }
   private Claims AuthenticatedUserByToken(String token) {
       Claims accessTokenInfo = tokenProvider.getUserInfoFromToken(token);
       setAuthentication(accessTokenInfo.getSubject());
       return accessTokenInfo;
   }
}