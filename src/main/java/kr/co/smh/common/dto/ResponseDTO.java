package kr.co.smh.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
    private int status;
    private String message;
}