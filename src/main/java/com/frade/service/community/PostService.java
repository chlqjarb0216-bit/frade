package com.frade.service.community;

import org.springframework.web.multipart.MultipartFile;

import com.frade.dto.community.PostDTO;

public interface PostService {
	
	//작성한 게시글 db에 저장
	public int savePost(PostDTO post, MultipartFile[] files);


	
}
