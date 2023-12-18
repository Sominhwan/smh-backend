package kr.co.smh.module.noticeBoard.dto;

import lombok.Data;

@Data
public class NoticeBoardCommentDTO {
	private int noticeCommentId;
	private int noticeId;
	private int userId;
	private String nickname;
	private String comment;
	private String createAt;
	private String profileUrl;
	private int likeCount;
	private int unlikeCount;
	private boolean likeFlag;
	private boolean unlikeFlag;
	private Integer noticeCommentLikeId;
	private Integer noticeCommentUnlikeId;
}
