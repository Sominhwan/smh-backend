package kr.co.smh.module.noticeBoard.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;

@Repository
@Mapper
public interface NoticeBoardDAO {
	Integer noticeBoardInsert(NoticeBoardVO vo);
}
