package com.frade.service.community.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frade.dto.community.PostDTO;
import com.frade.service.community.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Override
	public int savePost(PostDTO post, MultipartFile[] files) {
		
		return 1;
	}

	@Override
	public Map<String, Object> getPostList(int page) {
		//********테스트용 데이터**********
		// 1. 임시로 전체 105개의 가짜 데이터 생성
	    List<PostDTO> allPosts = new ArrayList<>();
	    for (int i = 105; i >= 1; i--) {
	        PostDTO post = new PostDTO();
	        post.setPNum((long) i);
	        post.setPCategoryNum(i % 3);
	        post.setPTitle("테스트 게시글 제목 " + i);
	        post.setUNum(1L);
	        post.setPViewCnt((int) (Math.random() * 100));
	        post.setPPostedDate(new java.sql.Date(System.currentTimeMillis()));
	        allPosts.add(post);
	    }

	    // 2. 페이징 수학 계산
	    int limit = 10; // 한 페이지에 보여줄 글 개수
	    int totalPosts = allPosts.size(); // 총 105개
	    int totalPages = (int) Math.ceil((double) totalPosts / limit); // 총 11페이지
	    
	    // 리스트 자르기 (subList)
	    int startIdx = (page - 1) * limit;
	    int endIdx = Math.min(startIdx + limit, totalPosts);
	    List<PostDTO> pagedList = allPosts.subList(startIdx, endIdx);

	    // 하단 페이징 블록 계산 (1~5, 6~10)
	    int blockSize = 5;
	    int startPage = ((page - 1) / blockSize) * blockSize + 1;
	    int endPage = Math.min(startPage + blockSize - 1, totalPages);

	    // 3. 자른 목록과 페이징 정보를 Map에 담아서 반환
	    Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("list", pagedList); // 10개의 데이터
	    resultMap.put("currentPage", page);
	    resultMap.put("totalPages", totalPages);
	    resultMap.put("startPage", startPage);
	    resultMap.put("endPage", endPage);
	  //********테스트용 데이터**********
	    return resultMap;
	}

}
