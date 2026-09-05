package com.frade.controller.community;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.frade.common.ResultCode;
import com.frade.dto.community.CommentDTO;
import com.frade.dto.community.PageResultDTO;
import com.frade.dto.community.PostDTO;
import com.frade.dto.rest.RestApiResponse;
import com.frade.service.community.CommentService;
import com.frade.service.community.PostService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController // 내부적으로 모든 메서드에 @ResponseBody가 적용됨
@RequestMapping("/api/community-lists") // 공통 API 경로 세팅
public class RestCommunityController {

	@Autowired
	private PostService postService;

	@Autowired
	private CommentService commentService;

	// 게시글 리스트 데이터 통신
	@GetMapping("/post-list")
	public RestApiResponse<PageResultDTO<PostDTO>> getPostListData(
            @RequestParam(defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "") String keyword,
			@RequestParam(defaultValue = "0") int type) {
        
		try {
            PageResultDTO<PostDTO> result = postService.getPostList(page, keyword, type);
			if (result.getTotalCount() > 0) {
				return RestApiResponse.success(result);
			} else {
				return RestApiResponse.response(ResultCode.SUC_EMPTY, null);
			}
		} catch (Exception e) {
			return RestApiResponse.error(ResultCode.FAIL);
		}
	}

	// 댓글 리스트 데이터 통신
	@GetMapping("/comment-list")
	public RestApiResponse<PageResultDTO<CommentDTO>> getCommentListData(
            @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "1") int postNum) {

		try {
            PageResultDTO<CommentDTO> result = commentService.getCommentList(postNum, page);
			if (result.getTotalCount() > 0) {
				return RestApiResponse.success(result);
			} else {
				return RestApiResponse.response(ResultCode.SUC_EMPTY, null);
			}
		} catch (Exception e) {
			return RestApiResponse.error(ResultCode.FAIL);
		}
	}

	// 댓글 작성 데이터 통신
	@PostMapping("/comment-write")
	public RestApiResponse<?> saveComment(@Valid @RequestBody CommentDTO comment, BindingResult br, HttpSession session) {

		if (br.hasErrors()) {
			return RestApiResponse.error(ResultCode.COM_TEXT_FAIL);
		}

		Integer loginUserNum = (Integer) session.getAttribute("loginUserNum");
		if (loginUserNum == null) {
			loginUserNum = 1; // 로그인 임시 처리
		}
		comment.setUserNum(loginUserNum);

		int result = commentService.saveComment(comment);
		if (result > 0) {
			return RestApiResponse.success();
		} else {
			return RestApiResponse.error(ResultCode.FAIL);
		}
	}

	// 댓글 삭제 데이터 통신
	@PostMapping("/comment-delete")
	public RestApiResponse<?> deleteComment(@RequestParam("commentNum") int commentNum, HttpSession session) {

		// 현재 로그인한 사람의 세션 번호 가져오기
		Integer loginUserNum = (Integer) session.getAttribute("loginUserNum");

		if (loginUserNum == null) {
			loginUserNum = 1; // [TEST] 강제 로그인 번호 세팅
		}

		try {
			// DB에서 삭제하려는 댓글 정보 먼저 조회
			CommentDTO comment = commentService.getComment(commentNum);

			// 남의 댓글을 지우려고 하면 강제로 튕겨냄 (화면에서 비활성화 하지만 추가 검증)
			if (comment == null || comment.getUserNum() != loginUserNum) {
				return RestApiResponse.error(ResultCode.FAIL);
			}

			// 진짜 주인이 맞으면 삭제 진행
			int result = commentService.deleteComment(commentNum);
			if (result > 0) {
				return RestApiResponse.success();
			} else {
				return RestApiResponse.error(ResultCode.FAIL);
			}
		} catch (Exception e) {
			log.error("댓글 삭제 중 에러 발생", e);
			return RestApiResponse.error(ResultCode.FAIL);
		}
	}
}