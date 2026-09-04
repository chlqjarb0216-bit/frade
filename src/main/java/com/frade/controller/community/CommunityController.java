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
	        
	        // 쿠키 유지 시간 설정  24시간 (60초 * 60분 * 24시간)
	        newCookie.setMaxAge(60 * 60 * 24); 
	        newCookie.setPath("/"); // 모든 경로에서 이 쿠키 접근 허용
	        
	        response.addCookie(newCookie); // 사용자 브라우저에 쿠키 저장
	    }


	    PostDTO post = postService.getPost(postNum, isViewUp);
	    
	    model.addAttribute("post", post);
		return "community/detail";
	}
}