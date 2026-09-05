package com.frade.service.community;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.frade.dto.community.CommentDTO;
import com.frade.dto.community.PageResultDTO;

public interface CommentService {

	
	//댓글 리스트 데이터 요청
	public PageResultDTO<CommentDTO> selectCommentList(int postNum, int page);
	
	//댓글 데이터 db에 저장요청 및 응답
	public int saveComment(CommentDTO comment);
	
}
