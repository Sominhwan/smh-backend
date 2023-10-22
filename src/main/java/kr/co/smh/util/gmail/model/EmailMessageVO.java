package kr.co.smh.util.gmail.model;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmailMessageVO {
	private int userId;
	private String content;
	private Timestamp createAt;
}
