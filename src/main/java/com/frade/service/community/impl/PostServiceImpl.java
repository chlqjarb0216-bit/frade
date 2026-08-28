package com.frade.service.community.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frade.dto.community.PostDTO;
import com.frade.service.community.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Override
	public int savePost(PostDTO post, MultipartFile[] files) {
		
		return 1;
	}

	@Override
	public int validateText(PostDTO post) {
		return 1;
		
	}

}
