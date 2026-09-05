package com.frade.dao.community.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.community.CommentDAO;
import com.frade.dto.community.CommentDTO;


@Repository
public class CommentDAOImpl implements CommentDAO{

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int saveComment(CommentDTO comment) {
		int result = sqlSessionTemplate.insert("comment_mapper.saveComment",comment);
		return result;
	}

	
}
