package kr.co.smh.util.amazonS3.controller;

import java.io.IOException;

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
import kr.co.smh.common.service.ByteCaculation;
import kr.co.smh.util.amazonS3.model.FileVO;
import kr.co.smh.util.amazonS3.service.AmazonS3Service;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/upload")
public class AmazonS3Controller {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final AmazonS3Service amazonS3Service;
	
	// 프로필 이미지 단일 저장
	@PostMapping(value="/profile")
	public HttpEntity<?> profileImageUpload(@RequestPart(value = "key") FileVO vo, @RequestPart(value = "file", required = false) MultipartFile file) {
		return amazonS3Service.uploadSingleFile(file, vo);
	}
}
