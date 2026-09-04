package com.frade.dao.community.impl;

import java.util.List;
import java.util.Map;

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
	public int selectPostTotalCount(Map<String, Object> params) {
			int result = sqlSessionTemplate.selectOne("post_mapper.selectPostTotalCount", params);
		
		return result;
	}

	@Override
	public List<PostDTO> selectPostList(Map<String, Object> params) {
		List<PostDTO> result = sqlSessionTemplate.selectList("post_mapper.selectPostList", params);
		return result;
	}

	@Override
	public PostDTO selectPost(int postNum) {
		
		return null;
	}

	@Override
	public int updateViewCount(int postNum) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
