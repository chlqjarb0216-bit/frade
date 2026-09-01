package com.frade.service.community;

import java.util.Map;

import org.springframework.stereotype.Service;

public interface CommentService {

	
	//댓글 리스트 데이터 요청
	public Map<String, Object> getCommentList(int postNum, int page);
	
}
