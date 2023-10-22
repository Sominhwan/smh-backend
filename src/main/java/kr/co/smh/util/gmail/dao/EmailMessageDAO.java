package kr.co.smh.util.gmail.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.util.gmail.model.EmailMessageVO;

@Repository
@Mapper
public interface EmailMessageDAO {
	// Sms 내용 저장
	void insertEmailContent(EmailMessageVO emailMessageVO);
}
