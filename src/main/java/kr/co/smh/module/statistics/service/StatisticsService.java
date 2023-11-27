package kr.co.smh.module.statistics.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.statistics.dao.StatisticsDAO;
import kr.co.smh.module.statistics.model.ThreadViewDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final StatisticsDAO statisticsDAO;
	public HttpEntity<?> threadView(int userId, String insertDate) {
		log.info("userId --> " + userId);
		log.info("insertDate --> " + insertDate);
		ThreadViewDTO threadViewDTO = statisticsDAO.threadView(userId, insertDate);
		log.info("threadViewDTO --> " + threadViewDTO);
		return new ResponseEntity<>(
				ResDTO.builder()
					  .code(0)
					  .message("파일이 성공적으로 업로드 되었습니다.")
					  .build(),
					  HttpStatus.OK);	
	}
}
