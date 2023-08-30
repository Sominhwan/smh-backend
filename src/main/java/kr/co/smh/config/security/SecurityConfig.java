package kr.co.smh.config.security;

import java.util.List;

import javax.servlet.DispatcherType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.co.smh.module.auth.service.AuthService;
import lombok.RequiredArgsConstructor;

@PropertySource("classpath:jwt.yml")
@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // 스프링 시큐리티 기능 비활성화
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web) -> web.ignoring()
//                .dispatcherTypeMatchers("/static/**");
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity             // SecurityFilterChain에서 요청에 접근할 수 있어서 인증, 인가 서비스에 사용
            .httpBasic().disable()      // http basic auth 기반으로 로그인 인증창이 뜬다. 기본 인증을 이용하지 않으려면 .disable()을 추가해준다.
            .csrf().disable()       // csrf, api server이용시 .disable (html tag를 통한 공격)
            .cors()	 //  다른 도메인의 리소스에 대해 접근이 허용되는지 체크
            .and()   // 묶음 구분(httpBasic(),crsf,cors가 한묶음)
            .authorizeRequests()    // 각 경로 path별 권한 처리
            .antMatchers("/api/v1/**").permitAll()         // 안에 작성된 경로의 api 요청은 인증 없이 모두 허용한다.
            .and()
            .sessionManagement()        // 세션 관리 기능을 작동한다.      .maximunSessions(숫자)로 최대 허용가능 세션 수를 정할수 있다.(-1로 하면 무제한 허용)
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀(STATELESS는 인증 정보를 서버에 담지 않는다.)
            .and()
            .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
            //UserNamePasswordAuthenticationFilter적용하기 전에 JWTTokenFilter를 적용 하라는 뜻 입니다.
            .build();
}
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:8080", "http://localhost:8090"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}