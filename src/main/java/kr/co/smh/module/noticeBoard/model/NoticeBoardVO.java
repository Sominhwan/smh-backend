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
	private String title;
	private String writer;
	private String content;
	private String insertDate;
	private String updateDate;
	private String deleteDate;
}
