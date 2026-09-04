package com.frade.controller.community;

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
	public RestApiResponse<?> saveComment(@Valid @RequestBody CommentDTO comment, BindingResult br) {

		if (br.hasErrors()) {
			return RestApiResponse.error(ResultCode.COM_TEXT_FAIL);
		}

		comment.setUserNum(1); // 로그인 임시 처리

		int result = commentService.saveComment(comment);
		if (result > 0) {
			return RestApiResponse.success();
		} else {
			return RestApiResponse.error(ResultCode.FAIL);
		}
	}
}