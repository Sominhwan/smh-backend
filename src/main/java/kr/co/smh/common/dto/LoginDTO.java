package kr.co.smh.common.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    private int status;
    private String message;
    private String accessToken;
    private LocalDateTime expiredAt;          //만료 시간
    private String refreshToken;
    private LocalDateTime issuedAt;           //발급 시간
}