package com.frade.dao.community.Impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.community.PostDAO;
import com.frade.dto.community.PostDTO;

@Repository
public class PostDAOImpl implements PostDAO{
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	public long getNextPostNum() {

		long result = sqlSessionTemplate.selectOne("post_mapper.getNextPostNum");
	
		return result;
	}

	@Override
	public int insertPost(PostDTO post) {

		int result = sqlSessionTemplate.insert("post_mapper.insertPost", post);
		return result;
	}

	@Override
	public int selectPostTotalCount(String keyword, int type) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<PostDTO> selectPostList(String keyword, int type, int offset, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PostDTO selectPost(int postNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateViewCount(int postNum) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
