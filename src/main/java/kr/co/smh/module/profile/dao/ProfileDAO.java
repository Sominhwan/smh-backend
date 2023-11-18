package kr.co.smh.module.profile.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.noticeBoard.model.ProfileVO;

@Mapper
@Repository
public interface ProfileDAO {
	// 업로드 이미지 가져오기
	List<ProfileVO> getUploadImage(int userId);
	// 프로필 이미지 가져오기
	List<ProfileVO> getProfileImage(int userId);
}
