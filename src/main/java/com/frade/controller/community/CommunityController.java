package com.frade.controller.community;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.frade.common.FilePath;
import com.frade.common.ResultCode;
import com.frade.dto.community.PostDTO;
import com.frade.service.community.PostService;
import com.frade.util.LoginManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
	public String write(HttpSession session) {
		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}
		return "community/write";
	}

	// 게시글 작성 처리
	@PostMapping("/write")
	public String writeAction(@Valid PostDTO post, BindingResult br,
			@RequestParam(value = "uploadFiles", required = false) MultipartFile[] files,
														HttpSession session,
														RedirectAttributes rttr) {

		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}

		int loginUserNum = LoginManager.getLoginUserNum(session);
		post.setUserNum(loginUserNum);
		
		if (br.hasErrors()) {
			return "redirect:/community-lists/write?error=true";
		}

		try {
			
			postService.savePost(post, files);
			return "redirect:/community-lists";
			
		} catch (Exception e) {
			
			log.error(e.getMessage());
			rttr.addFlashAttribute("msg",ResultCode.POST_WRT_FAIL.getMessage());
			
			return "redirect:/community-lists";
		}
	}

	// 게시글 상세 페이지 이동
	@GetMapping("/detail")
	public String postDetail(@RequestParam int postNum, Model model,
								HttpServletRequest request,
								HttpServletResponse response) {
		
		boolean isViewUp = true; // 기본적으로는 조회수를 올린다고 가정
	    Cookie[] cookies = request.getCookies();
	    String viewedPosts = "";

	    // 기존 쿠키들 중에서 "viewedPosts"가 있는지 검사
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals("viewedPosts")) {
	                viewedPosts = cookie.getValue();
	                
	                // 쿠키 값에 현재 글 번호가 포함되어 있다면? (예: "[105][106]")
	                if (viewedPosts.contains("[" + postNum + "]")) {
	                    isViewUp = false; // 이미 읽은 글 => 조회수 카운트 xx
	                }
	                break;
	            }
	        }
	    }

	    //  처음 읽는 글이라면 (true) 쿠키에 글 번호를 추가해서 다시 구워줌
	    if (isViewUp) {
	        // 기존 쿠키 문자열에 새 글 번호를 누적 (숫자가 겹치지 않게 대괄호 사용)
	        viewedPosts += "[" + postNum + "]";
	        Cookie newCookie = new Cookie("viewedPosts", viewedPosts);
	        
	        response.addCookie(newCookie); // 사용자 브라우저에 쿠키 저장
	    }


	    PostDTO post = postService.getPost(postNum, isViewUp);
	    
	    model.addAttribute("post", post);
		return "community/detail";
	}
	
	@PostMapping("/delete")
	public String deleteAction(@RequestParam("postNum") int postNum, HttpSession session) {
	    
	    if (!LoginManager.isLogin(session)) {
	        return "redirect:/user/login";
	    }
	    int loginUserNum = LoginManager.getLoginUserNum(session);

	    try {
	        // DB에서 삭제하려는 게시글 정보 먼저 조회
	        PostDTO post = postService.getPost(postNum, false);
	        
	        // 남의 글을 지우려고 하면 강제로 튕겨냄(화면에서 비활성화 하지만 추가 검증)
	        if (post == null || post.getUserNum() != loginUserNum) {
	            return "redirect:/community-lists/detail?postNum=" + postNum + "&error=auth"; 
	        }

	        // 4. 진짜 주인이 맞으면 삭제 진행
	        int result = postService.deletePost(postNum);
	        
	        if (result > 0) {
	            return "redirect:/community-lists"; 
	        } else {
	            return "redirect:/community-lists/detail?postNum=" + postNum + "&error=true";
	        }
	    } catch (Exception e) {
	        log.error("게시글 삭제 중 에러 발생", e);
	        return "redirect:/community-lists";
	    }
	}

	// 게시글 수정 페이지 이동 (GET)
	@GetMapping("/edit")
	public String edit(@RequestParam("postNum") int postNum,
					   HttpSession session,
					   Model model) {

		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}
		int loginUserNum = LoginManager.getLoginUserNum(session);

		PostDTO post = postService.getPost(postNum, false);
		if (post == null || post.getUserNum() != loginUserNum) {
			return "redirect:/community-lists/detail?postNum=" + postNum + "&error=auth";
		}

		model.addAttribute("post", post);
		return "community/write";
	}

	// 게시글 수정 처리 (POST)
	@PostMapping("/edit")
	public String editAction(@Valid PostDTO post, BindingResult br,
							 @RequestParam(value = "uploadFiles", required = false) MultipartFile[] files,
							 @RequestParam(value = "deleteExistingFiles", defaultValue = "false") boolean deleteExistingFiles,
							 HttpSession session,
							 RedirectAttributes rttr) {

		if (!LoginManager.isLogin(session)) {
			return "redirect:/user/login";
		}
		int loginUserNum = LoginManager.getLoginUserNum(session);

		if (post.getPostNum() == null) {
			return "redirect:/community-lists";
		}

		// 본인 게시글 검증
		PostDTO existingPost = postService.getPost(post.getPostNum().intValue(), false);
		if (existingPost == null || existingPost.getUserNum() != loginUserNum) {
			return "redirect:/community-lists/detail?postNum=" + post.getPostNum() + "&error=auth";
		}

		if (br.hasErrors()) {
			return "redirect:/community-lists/edit?postNum=" + post.getPostNum() + "&error=true";
		}

		try {
			post.setUserNum(loginUserNum);
			postService.updatePost(post, files, deleteExistingFiles);
			
			return "redirect:/community-lists/detail?postNum=" + post.getPostNum();
			
		} catch (Exception e) {
			log.error("게시글 수정 중 에러 발생", e);
			
			rttr.addFlashAttribute("msg",ResultCode.POST_MOD_FAIL.getMessage());
			return "redirect:/community-lists";
		}
	}
}