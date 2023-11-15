package kr.co.smh.util.amazonS3.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class FileVO {
	private int userId; 
	private String originFileName;
	private String saveFileName;
	private String fileUrl;
	private long fileSize;
	private String fileType;
	private Timestamp createAt;
}
