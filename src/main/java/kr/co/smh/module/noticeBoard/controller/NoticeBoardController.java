package kr.co.smh.module.noticeBoard.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
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
	 //private Logger log = LogManager.getLogger();
	private static final Logger log = LogManager.getLogger("kr.co.smh");


	private final NoticeBoardService noticeBoardService;
	@PostMapping(value="/write")
	public HttpEntity<?> noticeBoardWrite(@RequestBody NoticeBoardDTO.ReqNoticeBoard reqDTO) {
		log.info("noticeBoardWrtie -->");
		return noticeBoardService.noticeBoardWrite(reqDTO);			
	}
	
	@GetMapping(value="/search")
	public String noticeBoardList(@RequestParam("page") String page) {
		System.out.println(page);
		return "나는zzz";
	}
}
