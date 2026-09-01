package com.frade.service.community.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.frade.dto.community.CommentDTO;
import com.frade.service.community.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

	@Override
	public Map<String,Object> getCommentList(int postNum, int page) {

		//********테스트용 데이터(서버개발자는 참고해도 되고 지워도 됨)**********
		
				// 1. 임시로 전체 25개의 가짜 데이터 생성
			    List<CommentDTO> allComments = new ArrayList<>();
			    for (int i = 25; i >= 1; i--) {
			    	String content = "테스트 댓글 " + i;
			        String writer = "작성자" + (i % 5); // 작성자0 ~ 작성자4
			        
			        CommentDTO comment = new CommentDTO();
			        comment.setUserName(writer);
			        comment.setCommentContent(content);
			        comment.setPostCommentedDate(LocalDateTime.now());

			        allComments.add(comment);
			    }

			    // 2. 페이징 계산
			    int limit = 10; // 한 페이지에 보여줄 글 개수
			    int totalComments = allComments.size(); // 총 25개
			    //검색한 키워드가 없는경우 처리(없으면 1페이지)
			    int totalPages = totalComments == 0 ? 1 : (int) Math.ceil((double) totalComments / limit); // 총 3페이지
			    
			    // 리스트 자르기 (subList)
			    int startIdx = (page - 1) * limit;
			    int endIdx = Math.min(startIdx + limit, totalComments);
			    //총 검색된 게시글의 수가 최대페이징 첫게시글이(25개 조회됐으면 20) 같거나작다? => 아무것도 조회디지 않았다
			    List<CommentDTO> pagedList = startIdx >= totalComments ? new ArrayList<>() : allComments.subList(startIdx, endIdx);

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
			    
			    //댓글 총 개수 저장해서 보내기
			    resultMap.put("totalCount", allComments.size());
			    
			  //********테스트용 데이터**********
		
		
		return resultMap;
	}

	@Override
	public int saveComment(CommentDTO comment) {
		
		int result = 1;
		
		return result;
	}

}
