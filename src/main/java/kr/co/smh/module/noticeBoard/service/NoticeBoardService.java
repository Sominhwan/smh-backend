package kr.co.smh.module.noticeBoard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.noticeBoard.dao.NoticeBoardDAO;
import kr.co.smh.module.noticeBoard.dto.NoticeBoardCommentDTO;
import kr.co.smh.module.noticeBoard.dto.NoticeBoardDTO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardCommentVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardLikeVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {
	private final NoticeBoardDAO noticeBoardDAO;
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	// 글쓰기
	public HttpEntity<?> noticeBoardWrite(NoticeBoardDTO.ReqNoticeBoard reqDTO){
		noticeBoardDAO.noticeBoardInsert(reqDTO.boardInsert());
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("게시물이 등록되었습니다.")
						  .build(),
						  HttpStatus.OK);
	}	
	// 글 수정하기
	public HttpEntity<?> noticeBoardUpdate(NoticeBoardVO noticeBoardVO) {
		int result = noticeBoardDAO.noticeBoardUpdate(noticeBoardVO);
		log.info("글 수정 결과 -->" + result);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("게시물이 수정되었습니다.")
					  .build(),
					  HttpStatus.OK);
	}
	// 글 가져오기
	public HttpEntity<?> boardCotent(NoticeBoardVO noticeBoardVO) {
		NoticeBoardVO vo = noticeBoardDAO.boardCotent(noticeBoardVO);

		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("게시물을 가져왔습니다.")
					  .data(vo)
					  .build(),
					  HttpStatus.OK);
	}
	// 공지사항 전체페이지 수 
	public HttpEntity<?> noticeBoardPage(){	
		Integer totalPage = noticeBoardDAO.noticeBoardTotalPage();
		log.info("NoticetotalPage --> " + totalPage);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("게시물이 등록되었습니다.")
						  .data(totalPage)
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 글 목록
	public HttpEntity<?> noticeBoardSelect(String page, String category){
		if(page == null) page = "1";
		int currentPage = Integer.parseInt(page);
		int rowSize = 15;
		int offset = (currentPage - 1) * rowSize;
		
		Map<String, Object> map = new HashMap<String, Object>();
		Integer totalPage = noticeBoardDAO.noticeBoardTotalPage();
		List<NoticeBoardVO> noticeBoardList = noticeBoardDAO.noticeBoardList(offset, rowSize, category);
		
		map.put("totalPage", totalPage);
		map.put("noticeBoardList", noticeBoardList);
		
		log.info("noticeBoardList --> " + map);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 목록을 가져왔습니다.")
						  .data(map)
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 상제페이지 정보
	public HttpEntity<?> noticeBoardDetailSelect(NoticeBoardVO noticeBoardVO){
		NoticeBoardVO noticeBoardDetail = noticeBoardDAO.noticeBoardDetail(noticeBoardVO);
		log.info("noticeBoardDetail --> " + noticeBoardDetail);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 상세페이지 정보를 가져왔습니다.")
						  .data(noticeBoardDetail)
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 상제페이지 공지사항 리스트
	public HttpEntity<?> noticeBoardDetailList(String id){
		List<NoticeBoardVO> noticeBoardDetailList = noticeBoardDAO.noticeBoardDetailList(Integer.parseInt(id));
		log.info("noticeBoardDetailList --> " + noticeBoardDetailList);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 상세페이지 공지사항 리스트를 가져왔습니다.")
						  .data(noticeBoardDetailList)
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 상세페이지 삭제하기
	public HttpEntity<?> deleteNoticeBoardDetail(NoticeBoardVO noticeBoardVO) {
		int result = noticeBoardDAO.deleteNoticeBoardDetail(noticeBoardVO);
		log.info("공지사항 삭제 결과 --> " + result);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("공지사항 상세페이지를 삭제했습니다.")
					  .build(),
					  HttpStatus.OK);
	}
	// 공지사항 댓글 가져오기
	public HttpEntity<?> noticeBoardCommentList(NoticeBoardCommentVO noticeBoardCommentVO) {
		List<NoticeBoardCommentDTO> noticeBoardCommentVOList = noticeBoardDAO.noticeBoardCommentList(noticeBoardCommentVO);
		log.info("공지사항 댓글 리스트 --> " + noticeBoardCommentVOList);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("공지사항 상세페이지 댓글을 가져왔습니다.")
					  .data(noticeBoardCommentVOList)
					  .build(),
					  HttpStatus.OK);
	}
	// 공지사항 댓글 작성
	public HttpEntity<?> noticeBoardComment(NoticeBoardCommentVO noticeBoardCommentVO) {
		noticeBoardDAO.noticeBoardComment(noticeBoardCommentVO);
		//log.info("댓글 작성 결과 --> " + result);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("댓글 작성 완료.")
					  .build(),
					  HttpStatus.OK);
	}
	// 공지사항 댓글 수정
	public HttpEntity<?> updateNoticeBoardComment(NoticeBoardCommentVO noticeBoardCommentVO) {
		int result = noticeBoardDAO.updateNoticeBoardComment(noticeBoardCommentVO);
		log.info("댓글 수정 결과 --> " + result);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("댓글 작성 수정완료.")
					  .build(),
					  HttpStatus.OK);
	}
	// 공지사항 댓글 삭제
	public HttpEntity<?> deleteNoticeBoardComment(NoticeBoardCommentVO noticeBoardCommentVO) {
		noticeBoardDAO.deleteNoticeBoardComment(noticeBoardCommentVO);
		//log.info("댓글 삭제 결과 --> " + result);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("댓글이 삭제되었습니다.")
					  .build(),
					  HttpStatus.OK);
	}
	// 공지사항 좋아요 활성화
	public HttpEntity<?> likeNoticeBoard(NoticeBoardLikeVO noticeBoardLikeVO) {
		int likeCount = noticeBoardDAO.likeNoticeBoard(noticeBoardLikeVO);
		log.info("공지사항 좋아요 개수 --> " + likeCount);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("좋아요 활성화.")
					  .data(likeCount)
					  .build(),
					  HttpStatus.OK);
	}
	// 공지사항 좋아요 비활성화
	public HttpEntity<?> unlikeNoticeBoard(NoticeBoardLikeVO noticeBoardLikeVO) {
		int likeCount = noticeBoardDAO.unlikeNoticeBoard(noticeBoardLikeVO);
		log.info("공지사항 좋아요 개수 --> " + likeCount);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("좋아요 비활성화.")
					  .data(likeCount)
					  .build(),
					  HttpStatus.OK);
	}
}
