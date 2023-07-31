package kr.co.smh.module.noticeBoard.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.module.noticeBoard.dto.NoticeBoardDTO;
import kr.co.smh.module.noticeBoard.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/noticeboard")
public class NoticeBoardController {
	private final NoticeBoardService noticeBoardService;
	@PostMapping(value="/search")
	public HttpEntity<?> search(@RequestBody NoticeBoardDTO.ReqNoticeBoard reqDTO) {
		return noticeBoardService.noticeBoardWrite(reqDTO);			
	}
}
