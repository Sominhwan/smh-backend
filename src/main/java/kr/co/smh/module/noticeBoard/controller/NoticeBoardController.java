package kr.co.smh.module.noticeBoard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.module.noticeBoard.dto.NoticeBoardDTO;
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
	public HttpEntity<?> noticeBoardDetail(@Nullable @RequestParam("id") String id) {
		log.info("noticeBoardDetail --> id: " + id);
		return noticeBoardService.noticeBoardDetailSelect(id);
	}
	// 공지사항 상세페이지 공지사항 리스트 
	@GetMapping(value="/detail/list")
	public HttpEntity<?> noticeBoardDetailList(@Nullable @RequestParam("id") String id) {
		log.info("noticeBoardDetailList --> id: " + id);
		return noticeBoardService.noticeBoardDetailList(id);
	}
}
