package kr.co.smh.module.noticeBoard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;

@Repository
@Mapper
public interface NoticeBoardDAO {
	// 공지사항 글쓰기
	Integer noticeBoardInsert(NoticeBoardVO vo);
	// 공지사항 글목록 가져오기
	List<NoticeBoardVO> noticeBoardList(@Param("offset") int offset, @Param("rowSize") int rowSize, @Param("category") String category);
	// 공지사항 전체글 개수
	Integer noticeBoardTotalPage();
	// 공지사항 상세보기 정보 가져오기
	List<NoticeBoardVO> noticeBoardDetailList(@Param("id") int id, @Param("category") String cateogory);	
}
