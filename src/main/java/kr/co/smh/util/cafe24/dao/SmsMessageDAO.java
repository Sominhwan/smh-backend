package kr.co.smh.util.cafe24.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.smh.util.cafe24.model.SmsMessageVO;

@Repository
@Mapper
public interface SmsMessageDAO {
	// Sms 내용 저장
	void insertSmsContent(SmsMessageVO smsMessageVO);
}
