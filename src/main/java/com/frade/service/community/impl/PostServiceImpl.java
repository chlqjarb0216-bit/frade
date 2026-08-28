package com.frade.service.community.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.frade.dto.community.PostDTO;
import com.frade.service.community.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Override
	public void savePost(PostDTO post, MultipartFile[] files) {
		
		
	}

}
