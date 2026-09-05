package com.frade.dao.community;

import com.frade.dto.community.CommentDTO;

public interface CommentDAO {

	public int saveComment(CommentDTO comment);
}
