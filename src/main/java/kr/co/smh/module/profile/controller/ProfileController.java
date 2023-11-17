package kr.co.smh.module.profile.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.common.dto.ResDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/profile")
public class ProfileController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	
	// 업로드 이미지 가져오기
	@GetMapping(value="/upload_image")
	public HttpEntity<?> uploadImage(@Nullable @RequestParam String userId) {
		log.info("업로드 이미지 userId --> " + userId);
		
  		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("파일이 성공적으로 업로드 되었습니다..")
					  .build(),
					  HttpStatus.OK);
	}
	// 프로필 이미지 가져오기
	@GetMapping(value="/image")
	public HttpEntity<?> profileImage(@Nullable @RequestParam String userId) {
		log.info("프포릴 이미지 userId --> " + userId);
		
  		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("파일이 성공적으로 업로드 되었습니다..")
					  .build(),
					  HttpStatus.OK);
	}
}
