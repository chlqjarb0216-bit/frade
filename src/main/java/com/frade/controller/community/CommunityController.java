package com.frade.controller.community;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.frade.common.FilePath;
import com.frade.dto.community.PostDTO;
import com.frade.service.community.PostService;

@Controller
@RequestMapping("/community-lists")
public class CommunityController {

	@Autowired
	private PostService postService;

	// 게시글 목록 페이지 이동
	@GetMapping("")
	public String lists() {
		return "community/lists";
	}

	// 게시글 작성 페이지 이동
	@GetMapping("/write")
	public String write(PostDTO post, @RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("msg", "게시글 작성 중 서버 오류가 발생했습니다.");
		}
		return "community/write";
	}

	// 게시글 작성 처리
	@PostMapping("/write")
	public String writeAction(@Valid PostDTO post, BindingResult br,
			@RequestParam(value = "uploadFiles", required = false) MultipartFile[] files) {

		if (br.hasErrors()) {
			return "redirect:/community-lists/write?error=true";
		}

		post.setUserNum(1); // (test) 로그인 가정

		try {
			int result = postService.savePost(post, files);
			if (result > 0) {
				return "redirect:/community-lists"; 
			} else {
				return "redirect:/community-lists/write?error=true"; 
			}
		} catch (Exception e) {
			e.printStackTrace(); 
			return "redirect:/community-lists/write?error=true";
		}
	}

	// 게시글 상세 페이지 이동
	@GetMapping("/detail")
	public String postDetail(@RequestParam int postNum, Model model) {
		model.addAttribute("post", postService.getPost(postNum));
		model.addAttribute("path", FilePath.FILE_ROOT_PATH + FilePath.POST_UPLOADFILE_PATH);
		return "community/detail";
	}
}