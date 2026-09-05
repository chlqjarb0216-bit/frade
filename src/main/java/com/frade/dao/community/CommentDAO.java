package com.frade.dao.community;

import java.util.List;
import java.util.Map;

import com.frade.dto.community.CommentDTO;

public interface CommentDAO {

	// 댓글 저장
	public int saveComment(CommentDTO comment);
	
	// 특정 게시글의 전체 댓글 개수
	public int selectCommentTotalCount(int postNum);
	
	// 특정 게시글의 페이징 처리된 댓글 목록
	public List<CommentDTO> selectCommentList(Map<String, Object> params);
}
