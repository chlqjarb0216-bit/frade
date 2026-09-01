package com.frade.service.community.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

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
	public Map<String, Object> getPostList(int page, String keyword, int type) {
		//********테스트용 데이터(서버개발자는 참고해도 되고 지워도 됨)**********
		
		// 1. 임시로 전체 105개의 가짜 데이터 생성
	    List<PostDTO> allPosts = new ArrayList<>();
	    for (int i = 105; i >= 1; i--) {
	    	String title = "테스트 게시글 제목 " + i;
	        String writer = "작성자" + (i % 5); // 작성자0 ~ 작성자4
	        
	        // 검색어가 있으면 필터링 (type 0: 제목, type 1: 작성자)
	        if (!keyword.isEmpty()) {
	            if (type == 0 && !title.contains(keyword)) continue;
	            if (type == 1 && !writer.contains(keyword)) continue;
	        }
	        PostDTO post = new PostDTO();
	        post.setPostNum((long) i);
	        post.setPostCategoryNum(i % 3);
	        post.setPostTitle(title);
	        post.setUserNum((long)i%5);
	        post.setPostViewCnt((int) (Math.random() * 100));
	        post.setPostPostedDate(LocalDateTime.now());
	        allPosts.add(post);
	    }

	    // 2. 페이징 계산
	    int limit = 10; // 한 페이지에 보여줄 글 개수
	    int totalPosts = allPosts.size(); // 총 105개
	    //검색한 키워드가 없는경우 처리(없으면 1페이지)
	    int totalPages = totalPosts == 0 ? 1 : (int) Math.ceil((double) totalPosts / limit); // 총 11페이지
	    
	    // 리스트 자르기 (subList)
	    int startIdx = (page - 1) * limit;
	    int endIdx = Math.min(startIdx + limit, totalPosts);
	    //총 검색된 게시글의 수가 최대페이징 첫게시글이(38개 조회됐으면 31) 같거나작다? => 아무것도 조회디지 않았다
	    List<PostDTO> pagedList = startIdx >= totalPosts ? new ArrayList<>() : allPosts.subList(startIdx, endIdx);

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

	@Override
	public PostDTO getPost(int postNum) {
		
		//postNum을 키값으로 테이블 조회해서 게시글 정보 가져오기 
		//=========테스트데이터==============
		PostDTO post = new PostDTO();
		post.setPostNum(12L);
		post.setPostTitle("testTitle");
		post.setPostCategoryNum(2);
		post.setPostContent("testContent");
		post.setUserName("test개미");
		post.setPostLikeCnt(552);
		post.setPostViewCnt(123);
		post.setPostPostedDate(LocalDateTime.now());
		
		//=========테스트데이터==============
		
		return post;
	}


	
	

}
