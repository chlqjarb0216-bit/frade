package com.frade.service.community.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frade.dao.community.CommentDAO;
import com.frade.dto.community.CommentDTO;
import com.frade.dto.community.PageResultDTO;
import com.frade.service.community.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDAO commentDAO;

	@Override
	public PageResultDTO<CommentDTO> getCommentList(int postNum, int page) {
		int limit = 10; // 한 페이지에 보여줄 댓글 개수
		int offset = (page - 1) * limit; // 건너뛸 데이터 개수 (DB용)

		// MyBatis(SqlSessionTemplate)로 넘길 파라미터들을 Map에 포장
		Map<String, Object> params = new HashMap<>();
		params.put("postNum", postNum);
		params.put("offset", offset);
		params.put("limit", limit);

		// DB에서 해당 게시글의 전체 댓글 수 가져오기
		int totalComments = commentDAO.getCommentTotalCount(postNum);

		// DB에서 페이징 처리된 실제 댓글 리스트 가져오기
		List<CommentDTO> pagedList = new ArrayList<>();
		if (totalComments > 0 && offset < totalComments) {
			pagedList = commentDAO.selectCommentList(params);
		}

		// 페이징 로직 계산
		int totalPages = totalComments == 0 ? 1 : (int) Math.ceil((double) totalComments / limit);
		int blockSize = 5;
		int startPage = ((page - 1) / blockSize) * blockSize + 1;
		int endPage = Math.min(startPage + blockSize - 1, totalPages);

		// PageResultDTO 객체 반환
		return new PageResultDTO<>(
				pagedList,
				page,
				totalPages,
				startPage,
				endPage,
				totalComments
		);
	}

	@Override
	public int saveComment(CommentDTO comment) {
		int result = commentDAO.saveComment(comment);
		return result;
	}

	@Override
	public int getComment(int commentNum, int loginUserNum) {
		Map<String, Object> commentUserNum = new HashMap<>();
		commentUserNum.put("commentNum", commentNum);
		commentUserNum.put("userNum", loginUserNum);
		
		return commentDAO.isWriterComment(commentUserNum);
	}

	@Override
	public int deleteComment(int commentNum) {
		return commentDAO.deleteComment(commentNum);
	}

}
