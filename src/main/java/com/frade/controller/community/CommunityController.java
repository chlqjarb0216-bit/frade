package com.frade.controller.community;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.frade.dto.community.PostDTO;
import com.frade.service.community.PostService;

@Controller
@RequestMapping("/community-lists")
public class CommunityController {
	@Autowired
	PostService postService;

	@GetMapping("")
	public String lists() {
		
		
		return "community/lists";
	}
	
	
	@GetMapping("/write")
	public String write(PostDTO post) {
		//로그인 했다고 가정시켜주는 코드
		post.setUNum(00000001L);
		
		return "community/write";
	}
	
	@PostMapping("/write")
	//게시글 저장 버튼 클릭시 하단 컨트롤러 동작
	public String writeAction(PostDTO post, @RequestParam(value = "pFiles", required = false) MultipartFile[] files) {
		
		System.out.println(post); 
		/*PostDTO(pNum=null, uNum=null, pCategoryNum=null, scNum=null,
		   pTitle=test1, pContent=testest2222, pViewCnt=null, pLikeCnt=null,
		   pPostedDate=null, pUpdatedDate=null, pTrNum1=null, pTrNum2=null,
		   pTrNum3=null, pFiles=null, pIsPublic=null)
		 */
		
		postService.savePost(post);
		
		
		return "community/lists";
	}
}
