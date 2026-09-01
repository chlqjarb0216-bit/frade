package com.frade.controller.community;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.frade.dto.community.CommentDTO;
import com.frade.dto.community.PostDTO;
import com.frade.service.community.CommentService;
import com.frade.service.community.PostService;

@Controller
@RequestMapping("/community-lists")
public class CommunityController {
	@Autowired
	PostService postService;
	
	@Autowired
	CommentService commentService;

	@GetMapping("")
	public String lists() {
		

		return "community/lists";
	}
	
	
	@GetMapping("/api/post-list")
    @ResponseBody
    public Map<String, Object> getPostListData(@RequestParam(defaultValue = "1") int page,
								    		@RequestParam(required = false, defaultValue = "") String keyword,
								    		@RequestParam(defaultValue = "0") int type) {
		
        // 서비스에서 10개의 글과 페이징 정보(Map)를 가져옴
        Map<String, Object> result = postService.getPostList(page, keyword, type);
        
        return result;
    }
	
	@GetMapping("/write")
	public String write(PostDTO post, @RequestParam(value = "error", required = false) String error, Model model) {
		
		//게시글 저장 서버 오류 발생 예외처리
		if (error != null) {
	        model.addAttribute("msg", "게시글 작성 중 서버 오류가 발생했습니다.");
	    }
		
		return "community/write";
	}
	
	@PostMapping("/write")
	//게시글 저장 버튼 클릭시 하단 컨트롤러 동작
	public String writeAction(@Valid PostDTO post, BindingResult br,
			@RequestParam(value = "uploadFiles", required = false) MultipartFile[] files) {
		
		//@Valid 검증 실패 시 처리
	    if (br.hasErrors()) {

	        return "redirect:/community-lists/write?error=true";
	    }
	    
		//(test)로그인 했다고 가정시켜주는 코드
		post.setUserNum(1L);
		
		System.out.println(post); 
		/*PostDTO(pNum=null, uNum=null, pCategoryNum=null, scNum=null,
		   pTitle=test1, pContent=testest2222, pViewCnt=null, pLikeCnt=null,
		   pPostedDate=null, pUpdatedDate=null, pTrNum1=null, pTrNum2=null,
		   pTrNum3=null, pFiles=null, pIsPublic=null)
		 */
		
		//(test)다중 파일이 잘 전달 되는지 확인 
		if (files != null) {
		    for (int i = 0; i < files.length; i++) {
		        System.out.println((i + 1) + "번째 파일명: " + files[i].getOriginalFilename());
		        System.out.println("크기: " + files[i].getSize() + " bytes");
		    }
		}
		
		
		    int result = postService.savePost(post, files);
		    if(result>0) {
		    	return "redirect:/community-lists";
		    } else {
		    	return "redirect:/community-lists/write?error=true";
		    }	
	}
	
	
	@GetMapping("/detail")
	public String postDetail(@RequestParam int postNum, Model model) {
		
		
		System.out.println(postService.getPost(postNum));
		model.addAttribute("post", postService.getPost(postNum));
		
		
		return "community/detail";
	}
	
	@GetMapping("/api/comment-list")
	@ResponseBody
	public Map<String, Object> getCommentListData(@RequestParam(defaultValue = "1") int page,
												@RequestParam(required = false, defaultValue = "1") int postNum) {
		
		System.out.println("컨트롤전달완료"+postNum);
		
//		List<CommnetDTO> commentList = new ArrayList<CommnetDTO>();//테스트용
//		List<CommentDTO> result = commentService.getCommentList(postNum, page);
		Map<String, Object> result = commentService.getCommentList(postNum, page);
		
		return result;
	}
	
	
}
