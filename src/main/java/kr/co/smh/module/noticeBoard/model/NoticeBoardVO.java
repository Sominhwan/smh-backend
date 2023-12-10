package kr.co.smh.module.noticeBoard.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class NoticeBoardVO {
	private int noticeId;
	private Integer userId;
	private String title;
	private String writer;
	private String content;
	private int viewsCount;
	private int likeCount;
	private int commentCount;
	private String createAt;
	private String updateAt;
	private String delYn;	
	
	private boolean likeCheck;
}