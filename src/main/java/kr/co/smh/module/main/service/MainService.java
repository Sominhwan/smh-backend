package kr.co.smh.module.main.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.main.dao.MainDAO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainService {
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	private final MainDAO mainDAO;
	
	public HttpEntity<?> boardSelect() {
		List<NoticeBoardVO> vo = mainDAO.noticeBoardList();
		log.info("noticeBoardList --> " + vo);
		return new ResponseEntity<>(
				ResDTO.builder()
						.code(0)
						.message("게시판 목록을 불러왔습니다.")
						.data(vo)
						.build(),
						HttpStatus.OK);
				
	}
}
