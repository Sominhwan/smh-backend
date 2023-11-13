package kr.co.smh.util.amazonS3.model;

import lombok.Data;

@Data
public class FileVO {
	private String userId;
	private String originName;
	private String file;
}
