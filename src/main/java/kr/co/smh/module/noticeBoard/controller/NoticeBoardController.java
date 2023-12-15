package kr.co.smh.module.noticeBoard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.noticeBoard.dto.NoticeBoardDTO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardCommentLikeVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardCommentVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardLikeVO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;
import kr.co.smh.module.noticeBoard.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/noticeboard")
public class NoticeBoardController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final NoticeBoardService noticeBoardService;
	// 글쓰기
	@PostMapping(value="/write")
	public HttpEntity<?> noticeBoardWrite(@Nullable @RequestBody NoticeBoardDTO.ReqNoticeBoard reqDTO) {
		log.info("noticeBoardWrite --> " + reqDTO);
		return noticeBoardService.noticeBoardWrite(reqDTO);			
	}
	// 글 수정하기
	@PutMapping(value="/update")
	public HttpEntity<?> noticeBoardUpdate(@Nullable @RequestBody NoticeBoardVO noticeBoardVO) {
		return noticeBoardService.noticeBoardUpdate(noticeBoardVO);
	}
	// 글 가져오기
	@GetMapping(value="/content")
	public HttpEntity<?> boardCotent(@Nullable @ModelAttribute NoticeBoardVO noticeBoardVO) {
		return noticeBoardService.boardCotent(noticeBoardVO);
	}
	// 공지사항 전체페이지 수 
	@GetMapping(value="/page")
	public HttpEntity<?> noticeBoardPage() {
		return noticeBoardService.noticeBoardPage();
	}
	// 공지사항 글 목록
	@GetMapping(value="/select")
	public HttpEntity<?> noticeBoardList(@Nullable @RequestParam("page") String page, @Nullable @RequestParam("category") String category) {
		log.info("noticeBoardList --> page: " + page + " category: " + category);
		return noticeBoardService.noticeBoardSelect(page, category);
	}
	// 공지사항 상세페이지 정보
	@GetMapping(value="/detail/select")
	public HttpEntity<?> noticeBoardDetail(@Nullable @ModelAttribute NoticeBoardVO noticeBoardVO) {
		log.info("noticeBoardDetail --> " + noticeBoardVO.getNoticeId());
		return noticeBoardService.noticeBoardDetailSelect(noticeBoardVO);
	}
	// 공지사항 상세페이지 공지사항 리스트 
	@GetMapping(value="/detail/list")
	public HttpEntity<?> noticeBoardDetailList(@Nullable @RequestParam("id") String id) {
		log.info("noticeBoardDetailList --> id: " + id);
		return noticeBoardService.noticeBoardDetailList(id);
	}
	// 공지사항 상세페이지 삭제하기
	@DeleteMapping(value="/detail")
	public HttpEntity<?> deleteNoticeBoardDetail(@Nullable @RequestBody NoticeBoardVO noticeBoardVO) {
		return noticeBoardService.deleteNoticeBoardDetail(noticeBoardVO);
	}
	// 공지사항 댓글 가져오기
	@GetMapping(value="/comment")
	public HttpEntity<?> noticeBoardCommentList(@Nullable @ModelAttribute NoticeBoardCommentVO noticeBoardCommentVO) {
		return noticeBoardService.noticeBoardCommentList(noticeBoardCommentVO);
	}
	// 공지사항 댓글 작성
	@PostMapping(value="/comment")
	public HttpEntity<?> noticeBoardComment(@Nullable @RequestBody NoticeBoardCommentVO noticeBoardCommentVO) {
		return noticeBoardService.noticeBoardComment(noticeBoardCommentVO);
	}
	// 공지사항 댓글 수정하기
	@PutMapping(value="/comment")
	public HttpEntity<?> updateNoticeBoardComment(@Nullable @RequestBody NoticeBoardCommentVO noticeBoardCommentVO) {
		return noticeBoardService.updateNoticeBoardComment(noticeBoardCommentVO);
	}
	// 공지사항 댓글 삭제하기 
	@DeleteMapping(value="/comment")
	public HttpEntity<?> deleteNoticeBoardComment(@Nullable @RequestBody NoticeBoardCommentVO noticeBoardCommentVO) {
		return noticeBoardService.deleteNoticeBoardComment(noticeBoardCommentVO);
	}
	// 공지사항 좋아요 활성화
	@PostMapping(value="/like")
	public HttpEntity<?> likeNoticeBoard(@Nullable @RequestBody NoticeBoardLikeVO noticeBoardLikeVO) {
		return noticeBoardService.likeNoticeBoard(noticeBoardLikeVO);
	}
	// 공지사항 좋아요 비활성화
	@PostMapping(value="/unlike")
	public HttpEntity<?> unlikeNoticeBoard(@Nullable @RequestBody NoticeBoardLikeVO noticeBoardLikeVO) {
		return noticeBoardService.unlikeNoticeBoard(noticeBoardLikeVO);
	}
	// 공지사항 댓글 좋아요 활성화
	@PostMapping(value="/comment/like")
	public HttpEntity<?> commentLikeNoticeBoard(@Nullable @RequestBody NoticeBoardCommentLikeVO noticeBoardCommentLikeVO) {
		return noticeBoardService.commentLikeNoticeBoard(noticeBoardCommentLikeVO);
	}
	// 공지사항 댓글 싫어요 활성화
}
