package kr.co.smh.util.amazonS3.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.util.amazonS3.model.FileVO;

@Mapper
@Repository
public interface AmazonS3DAO {
	// 파일 저장
	FileVO insertFile(FileVO vo);
	// 유저 프로필 저장
	void insertUserProfile(FileVO vo);
}
