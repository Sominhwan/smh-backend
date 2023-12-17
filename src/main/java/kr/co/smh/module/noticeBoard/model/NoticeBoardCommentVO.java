package kr.co.smh.module.noticeBoard.model;

import lombok.Data;

@Data
public class NoticeBoardCommentVO {
	private Integer noticeCommentId; 
	private int noticeId;
	private int userId;
	private String comment;
	private String createAt;
	private String updateAt;
	private String delYn;
	private String deleteAt;
	private int likeCount;
	private int unlikeCount;
}
