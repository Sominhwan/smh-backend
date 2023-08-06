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
	
	@PostMapping(value="/write")
	public HttpEntity<?> noticeBoardWrite(@RequestBody @Nullable NoticeBoardDTO.ReqNoticeBoard reqDTO) {
		log.info("noticeBoardWrite --> " + reqDTO);
		return noticeBoardService.noticeBoardWrite(reqDTO);			
	}
	
	@GetMapping(value="/select")
	public HttpEntity<?> noticeBoardList(@RequestParam("page") @Nullable String page) {
		log.info("noticeBoardList --> " + page);
		return noticeBoardService.noticeBoardSelect(page);
	}
	
	@GetMapping(value="/detail/select")
	public HttpEntity<?> noticeBoardDetailList(@RequestParam("id") @Nullable String id) {
		log.info("noticeBoardDetailList --> " + id);
		return noticeBoardService.noticeBoardDetailSelect(id);
	}
}
