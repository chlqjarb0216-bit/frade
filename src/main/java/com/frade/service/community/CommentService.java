package com.frade.service.community;

import java.util.List;

import com.frade.dto.community.CommentDTO;

public interface CommentService {

	public List<CommentDTO> getCommentList(int postNum, int page);
	
}
