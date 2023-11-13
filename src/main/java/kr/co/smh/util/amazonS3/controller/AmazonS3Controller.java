package kr.co.smh.util.amazonS3.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.util.amazonS3.model.FileVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/upload")
public class AmazonS3Controller {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	
	@PostMapping(value="/profile")
	public HttpEntity<?> profileImageUpload(@RequestPart(value = "key") FileVO vo, @RequestPart(value = "file", required = false) MultipartFile file) {
		log.info("파일 정보 --> " + vo);
		log.info("이미지 파일 --> " + file);
		log.info("이미지 확장자 --> " + file.getOriginalFilename());
		log.info("이미지 파일 --> " + file.getSize());
		log.info("이미지 파일 --> " + file.getContentType());
		log.info("이미지 파일 --> " + file.getResource());
		
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("회원가입이 완료되었습니다.")
					  .build(),
					  HttpStatus.OK);  
	}
}
