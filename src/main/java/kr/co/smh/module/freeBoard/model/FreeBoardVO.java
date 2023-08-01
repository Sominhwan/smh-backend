package kr.co.smh.module.freeBoard.model;

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
public class FreeBoardVO {
	private int noticeId;
	private String title;
	private String writer;
	private String content;
	private int viewsCount;
	private String insertDate;
	private String updateDate;
	private String deleteDate;	
}
