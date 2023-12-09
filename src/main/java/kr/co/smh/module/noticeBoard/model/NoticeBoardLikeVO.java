package kr.co.smh.module.noticeBoard.model;

import lombok.Data;

@Data
public class NoticeBoardLikeVO {
	private Integer noticeBoardLikeId;
	private int noticeId;
	private int userId;
	private boolean likeCheck;
}
