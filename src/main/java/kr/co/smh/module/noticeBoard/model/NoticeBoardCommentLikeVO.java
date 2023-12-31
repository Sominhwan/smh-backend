package kr.co.smh.module.noticeBoard.model;

import lombok.Data;

@Data
public class NoticeBoardCommentLikeVO {
	private Integer noticeCommentLikeId;
	private Integer noticeCommentUnlikeId;
	private Integer noticeId;
	private Integer noticeCommentId;
	private Integer userId;
	private String createAt;
	
	private boolean likeFlag;
	private boolean unlikeFlag;
}
