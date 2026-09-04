package com.frade.controller.community;

import javax.servlet.http.HttpSession;
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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/community-lists")
public class CommunityController {

	@Autowired
	private PostService postService;

	// 게시글 목록 페이지 이동
	@GetMapping("")
	public String lists(HttpSession session) {
		
		// [TEST] 강제 로그인 처리: 세션에 로그인 정보가 없다면 1번 유저로 세팅
				if(session.getAttribute("loginUserNum") == null) {
					session.setAttribute("loginUserNum", 1); // 확인하신 실제 테스트 유저 번호 입력
					System.out.println("테스트 로그인 완료! 유저 번호: 1");
				}
		
		return "community/lists";
	}

	// 게시글 작성 페이지 이동
	@GetMapping("/write")
	public String write( @RequestParam(value = "error", required = false) String error, Model model) {
		if (error != null) {
			model.addAttribute("msg", "게시글 작성 중 서버 오류가 발생했습니다.");
		}
		return "community/write";
	}

	// 게시글 작성 처리
	@PostMapping("/write")
	public String writeAction(@Valid PostDTO post, BindingResult br,
			@RequestParam(value = "uploadFiles", required = false) MultipartFile[] files,
														HttpSession session) {

		//임시 로그인 코드(테스트용)
		Integer loginUserNum = (Integer) session.getAttribute("loginUserNum");
		
		// 만약 세션이 날아갔거나 비정상 접근이면 튕겨냄
		if(loginUserNum == null) {
			return "redirect:/community-lists"; // 실제로는 로그인 페이지로 리다이렉트
		}
		
		post.setUserNum(loginUserNum); // 세션에서 꺼낸 번호를 DTO에 주입
		//임시 로그인 코드(테스트용)
		
		if (br.hasErrors()) {
			return "redirect:/community-lists/write?error=true";
		}

		try {
			int result = postService.savePost(post, files);
			if (result > 0) {
				return "redirect:/community-lists";
			} else {
				return "redirect:/community-lists/write?error=true";
			}
		} catch (Exception e) {

			log.error(e.getMessage());
			return "redirect:/community-lists/write?error=true";
		}
	}

	// 게시글 상세 페이지 이동
	@GetMapping("/detail")
	public String postDetail(@RequestParam int postNum, Model model) {
		model.addAttribute("post", postService.getPost(postNum));
		return "community/detail";
	}
}