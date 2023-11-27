package kr.co.smh.module.statistics.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/api/v1/statistics")
public class StatisticsController {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final StatisticsService statisticsService;
	
	@GetMapping(value="/thread/view")
	public HttpEntity<?> threadView(@RequestParam("userId") int userId, @RequestParam("insertDate") String insertDate) {
  		return statisticsService.threadView(userId, insertDate);
	}
	
	@GetMapping(value="/thread/view/chart")
	public HttpEntity<?> threadViewChart(@RequestParam("userId") String userId, @RequestParam("insertDate") String insertDate) {
  		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("파일이 성공적으로 업로드 되었습니다.")
					  .build(),
					  HttpStatus.OK);
	}
}
