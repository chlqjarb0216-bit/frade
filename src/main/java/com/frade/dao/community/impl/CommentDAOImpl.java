package com.frade.dao.community.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.community.CommentDAO;
import com.frade.dto.community.CommentDTO;

@Repository
public class CommentDAOImpl implements CommentDAO {

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int saveComment(CommentDTO comment) {
		int result = sqlSessionTemplate.insert("comment_mapper.saveComment", comment);
		return result;
	}

	@Override
	public int getCommentTotalCount(int postNum) {
		int result = sqlSessionTemplate.selectOne("comment_mapper.selectCommentTotalCount", postNum);
		return result;
	}

	@Override
	public List<CommentDTO> selectCommentList(Map<String, Object> params) {
		List<CommentDTO> result = sqlSessionTemplate.selectList("comment_mapper.selectCommentList", params);
		return result;
	}

	@Override
	public int isWriterComment(Map<String, Object> commentUserNum) {
		int result = sqlSessionTemplate.selectOne("comment_mapper.selectComment", commentUserNum);
		return result;
	}

	@Override
	public int deleteComment(int commentNum) {
		int result = sqlSessionTemplate.delete("comment_mapper.deleteComment", commentNum);
		return result;
	}

}
