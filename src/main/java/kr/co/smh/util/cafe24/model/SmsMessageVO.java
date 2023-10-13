package kr.co.smh.util.cafe24.model;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
public class SmsMessageVO {
	private int userId;
	private String content;
	private Timestamp createAt;
}
