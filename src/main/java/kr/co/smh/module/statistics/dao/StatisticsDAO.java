package kr.co.smh.module.statistics.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.co.smh.module.statistics.model.ThreadViewDTO;

@Mapper
@Repository
public interface StatisticsDAO {
	// 글쓴횟수, 조회수, 좋아요 수, 댓글 수 가져오기
	ThreadViewDTO threadView(@Param("userId") int userId, @Param("insertDate") String insertDate);
}
