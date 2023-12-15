package kr.co.smh.module.noticeBoard.model;

import lombok.Data;

@Data
public class NoticeBoardCommentUnlikeVO {
	private Integer noticeCommentUnlikeId;
	private Integer noticeId;
	private Integer noticeCommentId;
	private Integer userId;
	private String createAt;
}
