package com.frade.service.community;

import org.springframework.web.multipart.MultipartFile;

import com.frade.dto.community.PostDTO;

public interface PostService {
	
	//작성한 게시글 db에 저장
	public int savePost(PostDTO post, MultipartFile[] files);
	
	//게시글 제목 및 내용 글자수 검증(savePost 내부에서 실행)
	public int validateText(PostDTO post);

	
}
