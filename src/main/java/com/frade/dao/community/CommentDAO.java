package com.frade.dao.community;

import java.util.List;
import java.util.Map;

import com.frade.dto.community.CommentDTO;

public interface CommentDAO {

	// 댓글 저장
	public int saveComment(CommentDTO comment);
	
	// 특정 게시글의 전체 댓글 개수
	public int getCommentTotalCount(int postNum);
	
	// 특정 게시글의 페이징 처리된 댓글 목록
	public List<CommentDTO> selectCommentList(Map<String, Object> params);
	
	// 댓글 단건 조회 (본인 확인용)
	public int selectComment(Map<String,Object> commentUserNum);
	
	// 댓글 삭제
	public int deleteComment(int commentNum);
}
