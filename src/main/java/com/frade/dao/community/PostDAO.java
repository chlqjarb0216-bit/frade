package com.frade.dao.community;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.frade.dto.community.PostDTO;

public interface PostDAO {

	//글쓰기 관련
	public long getNextPostNum();
	public int insertPost(PostDTO post);
	
	// 리스트 및 페이징 (검색 조건 포함)
	public int selectPostTotalCount(Map<String, Object> params);
	public List<PostDTO> selectPostList(Map<String, Object> params);
    
    // 상세조회
	public PostDTO selectPost(int postNum);
	public int updateViewCount(int postNum);
	
	//메인페이지 표시 게시글
	public List<PostDTO> selectMainPosts(int pageSize);
}
