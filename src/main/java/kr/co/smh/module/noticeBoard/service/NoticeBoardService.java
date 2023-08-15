package kr.co.smh.module.noticeBoard.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static final Logger log = LogManager.getLogger("kr.co.smh");
	// 글쓰기
	public HttpEntity<?> noticeBoardWrite(NoticeBoardDTO.ReqNoticeBoard reqDTO){
		noticeBoardDAO.noticeBoardInsert(reqDTO.boardInsert());
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("게시물이 등록되었습니다.")
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 전체페이지 수 
	public HttpEntity<?> noticeBoardPage(){	
		Integer totalPage = noticeBoardDAO.noticeBoardTotalPage();
		log.info("totalPage --> " + totalPage);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("게시물이 등록되었습니다.")
						  .data(totalPage)
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 글 목록
	public HttpEntity<?> noticeBoardSelect(String page, String category){
		if(page == null) page = "1";
		int currentPage = Integer.parseInt(page);
		int rowSize = 15;
		int offset = (currentPage - 1) * rowSize;
		
		List<NoticeBoardVO> noticeBoardList = noticeBoardDAO.noticeBoardList(offset, rowSize, category);
		log.info("noticeBoardList --> " + noticeBoardList);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 목록을 가져왔습니다.")
						  .data(noticeBoardList)
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 상제페이지 정보
	public HttpEntity<?> noticeBoardDetailSelect(String id){
		List<NoticeBoardVO> noticeBoardDetail = noticeBoardDAO.noticeBoardDetail(Integer.parseInt(id));
		log.info("noticeBoardDetail --> " + noticeBoardDetail);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 상세페이지 정보를 가져왔습니다.")
						  .data(noticeBoardDetail)
						  .build(),
						  HttpStatus.OK);
	}	
	// 공지사항 상제페이지 공지사항 리스트
	public HttpEntity<?> noticeBoardDetailList(String id){
		List<NoticeBoardVO> noticeBoardDetailList = noticeBoardDAO.noticeBoardDetailList(Integer.parseInt(id));
		log.info("noticeBoardDetailList --> " + noticeBoardDetailList);
		
		return new ResponseEntity<>(
					ResDTO.builder()
						  .code(0)
						  .message("공지사항 상세페이지 공지사항 리스트를 가져왔습니다.")
						  .data(noticeBoardDetailList)
						  .build(),
						  HttpStatus.OK);
	}	
}
