package kr.co.smh.module.noticeBoard.dto;

import kr.co.smh.module.noticeBoard.model.NoticeBoardVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class NoticeBoardDTO {
	@Builder
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ReqNoticeBoard{
		private int noticeId;
		private String title;
		private Integer userId;
		private String writer;
		private String content;
		private int viewsCount;
		private String insertDate;
		private String updateDate;
		private String deleteDate;
		
		public NoticeBoardVO boardInsert() {
			return NoticeBoardVO.builder()
					.title(title)
					.writer(writer)
					.content(content)
					.userId(userId)
					.build();			
		}
	}
}
