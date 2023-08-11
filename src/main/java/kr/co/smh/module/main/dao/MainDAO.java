package kr.co.smh.module.main.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;

@Repository
@Mapper
public interface MainDAO {
	// 주요소식 최근 5개 데이터 가져오기
	
	// 공지사항 최근 5개 데이터 가져오기
	List<NoticeBoardVO> noticeBoardList();
	// 최근 게시물 최근 5개 데이터 가져오기
}
