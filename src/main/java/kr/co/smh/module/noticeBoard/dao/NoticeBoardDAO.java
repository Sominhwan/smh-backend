package kr.co.smh.module.noticeBoard.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.noticeBoard.dto.NoticeBoardCommentDTO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardCommentLikeVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardCommentUnlikeVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardCommentVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardLikeVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;

@Repository
@Mapper
public interface NoticeBoardDAO {
	// 공지사항 글쓰기
	Integer noticeBoardInsert(NoticeBoardVO vo);
	// 글 수정하기
	int noticeBoardUpdate(NoticeBoardVO noticeBoardVO);
	// 글 가져오기
	NoticeBoardVO boardCotent(NoticeBoardVO noticeBoardVO);
	// 공지사항 글목록 가져오기
	List<NoticeBoardVO> noticeBoardList(@Param("offset") int offset, @Param("rowSize") int rowSize, @Param("category") String category);
	// 공지사항 전체글 개수
	Integer noticeBoardTotalPage();
	// 공지사항 상세보기 정보 가져오기
	NoticeBoardVO noticeBoardDetail(NoticeBoardVO noticeBoardVO);	
	// 공지사항 상세보기 공지사항 리스트
	List<NoticeBoardVO> noticeBoardDetailList(@Param("id") int id);
	// 공지사항 상세페이지 삭제하기
	int deleteNoticeBoardDetail(NoticeBoardVO noticeBoardVO);
	// 공지사항 댓글 가져오기
	List<NoticeBoardCommentDTO> noticeBoardCommentList(NoticeBoardCommentVO noticeBoardCommentVO);
	// 공지사항 댓글 작성
	void noticeBoardComment(NoticeBoardCommentVO noticeBoardCommentVO);
	// 공지사항 댓글 수정
	int updateNoticeBoardComment(NoticeBoardCommentVO noticeBoardCommentVO);
	// 공지사항 댓글 삭제하기
	void deleteNoticeBoardComment(NoticeBoardCommentVO noticeBoardCommentVO);
	// 공지사항 좋아요 활성화 (좋아요 개수 리턴)
	int likeNoticeBoard(NoticeBoardLikeVO noticeBoardLikeVO);
	// 공지사항 좋아요 비활성화 (좋아요 개수 리턴)
	int unlikeNoticeBoard(NoticeBoardLikeVO noticeBoardLikeVO);
	// 공지사항 댓글 좋아요 활성
	void noticeBoardCommentLikeUp(NoticeBoardCommentLikeVO noticeBoardCommentLikeVO);
	// 공지사항 댓글 싫어요 활성
	void noticeBoardCommentUnlikeUp(NoticeBoardCommentUnlikeVO noticeBoardCommentUnlikeVO);
}
