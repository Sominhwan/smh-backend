package kr.co.smh.module.main.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.module.main.service.MainService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/main")
public class MainController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final MainService mainService;

	@GetMapping(value="/select")
	public HttpEntity<?> mainBoardList() {
		return mainService.boardSelect();
	}
}
