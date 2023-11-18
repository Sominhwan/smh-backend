package kr.co.smh.module.profile.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.noticeBoard.model.ProfileVO;
import kr.co.smh.module.profile.dao.ProfileDAO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final ProfileDAO profileDAO;
	// 업로드 이미지 가져오기
	public HttpEntity<?> uploadImage(int userId) {
		List<ProfileVO> uploadImageList = profileDAO.getUploadImage(userId);
		log.info("업로드 이미지 리스트 --> " + uploadImageList);
  		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("파일이 성공적으로 업로드 되었습니다.")
					  .data(uploadImageList)
					  .build(),
					  HttpStatus.OK);
	}
	// 프로필 이미지 가져오기
	public HttpEntity<?> profileImage(int userId) {
		List<ProfileVO> profileImageList = profileDAO.getProfileImage(userId);
		log.info("프로필 이미지 리스트 --> " + profileImageList);
  		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("파일이 성공적으로 업로드 되었습니다.")
					  .data(profileImageList)
					  .build(),
					  HttpStatus.OK);
	}
}
