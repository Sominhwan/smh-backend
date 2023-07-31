package kr.co.smh.module.noticeBoard.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.smh.module.noticeBoard.dao.NoticeBoardDAO;
import kr.co.smh.module.noticeBoard.dto.NoticeBoardDTO;
import kr.co.smh.module.noticeBoard.dto.ResDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeBoardService {
	private final NoticeBoardDAO noticeBoardDAO;
	
	public HttpEntity<?> noticeBoardWrite(NoticeBoardDTO.ReqNoticeBoard reqDTO){
		noticeBoardDAO.noticeBoardInsert(reqDTO.boardInsert());
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("게시물이 등록되었습니다.")
						  .build(),
						  HttpStatus.OK);
	}	
}
