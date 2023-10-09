package kr.co.smh.common.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import kr.co.smh.common.dto.ResDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReCaptchaService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	@Value("${recaptcha.secretKey}")
	private String SECRET_KEY;
	@Value("${recaptcha.url}")
	private String URL;
	
	public HttpEntity<?> reCaptcha(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("secret", SECRET_KEY);
        map.add("response", token);
        
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(map, headers);
		
		RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity  = restTemplate.exchange(
        		URL, // reCAPTCHA URL 
                HttpMethod.POST, // HTTP 메서드 (POST)
                requestEntity, // 요청 본문 및 헤더
                String.class // 응답 유형 (문자열로 예상)
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            log.info("reCAPTCHA result --> " + responseBody);
            
        } else {

        }
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .build(),
					  HttpStatus.OK);  
	}
}
