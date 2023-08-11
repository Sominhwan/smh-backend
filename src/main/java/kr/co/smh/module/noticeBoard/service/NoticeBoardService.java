package kr.co.smh.module.noticeBoard.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.co.smh.common.dto.ResDTO;
import kr.co.smh.module.noticeBoard.dao.NoticeBoardDAO;
import kr.co.smh.module.noticeBoard.dto.NoticeBoardDTO;
import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;
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
	
	public HttpEntity<?> noticeBoardSelect(String page, String category){
		if(page == null) page = "1";
		int currentPage = Integer.parseInt(page);
		int rowSize = 15;
		int offset = (currentPage - 1) * rowSize;
		
		Map<String, Object> map = new HashMap<>();
		List<NoticeBoardVO> noticeBoardList = noticeBoardDAO.noticeBoardList(offset, rowSize, category);
		Integer totalPage = noticeBoardDAO.noticeBoardTotalPage();
		
		map.put("noticeBoardList", noticeBoardList);
		map.put("totalPage", totalPage);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 목록을 가져왔습니다.")
						  .data(map)
						  .build(),
						  HttpStatus.OK);
	}	
	
	public HttpEntity<?> noticeBoardDetailSelect(String id, String category){
		List<NoticeBoardVO> noticeBoardDetailList = noticeBoardDAO.noticeBoardDetailList(Integer.parseInt(id), category);

		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 상세페이지 정보를 가져왔습니다.")
						  .data(noticeBoardDetailList)
						  .build(),
						  HttpStatus.OK);
	}	
}
