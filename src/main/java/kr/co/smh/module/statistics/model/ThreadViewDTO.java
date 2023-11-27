package kr.co.smh.module.statistics.model;

import lombok.Data;

@Data
public class ThreadViewDTO {
	private int writeCount; // 글쓴 횟수
	private int viewCount; // 조회수
	private int likeCount; // 좋아요 수
	private int commentCount; // 댓글 수
}
