package com.frade.service.community.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.frade.common.FilePath;
import com.frade.dao.community.PostDAO;
import com.frade.dto.community.PageResultDTO;
import com.frade.dto.community.PostDTO;
import com.frade.service.community.PostService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostDAO postDAO;
	
	@Override
	public int savePost(PostDTO post, MultipartFile[] files) {

		//게시글 시퀀스 먼저 따오기
		long nextNum = postDAO.getNextPostNum();
		
		post.setPostNum((long) nextNum); // DTO에 번호 세팅 

		// 파일이 있을 경우에만 조립 및 저장 로직 실행
		if (files != null && files.length > 0) {
			StringJoiner sj = new StringJoiner(",");

			// 물리적 저장 경로 세팅 (D:/fileStorage_Frade/post_uploadfiles)
			String baseDir = FilePath.FILE_ABSOLUTE_STORE_PATH + FilePath.POST_UPLOADFILE_PATH;
			File folder = new File(baseDir);
			if (!folder.exists()) {
				folder.mkdirs(); // 폴더가 없으면 생성
			}

			for (int i = 0; i < files.length; i++) {
				MultipartFile file = files[i];
				if (!file.isEmpty()) {
					
						// 원본 파일명에서 확장자 추출 (.jpg, .png 등)
						String originalName = file.getOriginalFilename();
						String ext = originalName.substring(originalName.lastIndexOf("."));

						// 파일명 조립 (예: 105-1.jpg)
						String savedFileName = nextNum + "_" + (i + 1) + ext;
						sj.add(savedFileName);

						// 물리적 폴더에 실제 파일 저장
						File saveFile = new File(baseDir, savedFileName);
						
						try {
							file.transferTo(saveFile);
						} catch (IllegalStateException | IOException e) {
							e.printStackTrace();
							log.warn(e.getMessage());
						}
				}
			}
			// 조립된 문자열 (예: 105-1.jpg,105-2.png)을 DTO에 세팅
			post.setPostFiles(sj.toString());
		}

		// DB에 INSERT
		return postDAO.insertPost(post); 
	}

	@Override
	public PageResultDTO<PostDTO> getPostList(int page, String keyword, int type) {
		//********테스트용 데이터(서버개발자는 참고해도 되고 지워도 됨)**********

		// 1. 임시로 전체 105개의 가짜 데이터 생성
		List<PostDTO> allPosts = new ArrayList<>();
		for (int i = 105; i >= 1; i--) {
			String title = "테스트 게시글 제목 " + i;
			String writer = "작성자" + (i % 5); // 작성자0 ~ 작성자4

			// 검색어가 있으면 필터링 (type 0: 제목, type 1: 작성자)
			if (!keyword.isEmpty()) {
				if (type == 0 && !title.contains(keyword))
					continue;
				if (type == 1 && !writer.contains(keyword))
					continue;
			}
			PostDTO post = new PostDTO();
			post.setPostNum((long) i);
			post.setPostCategoryNum(i % 3);
			post.setPostTitle(title);
			post.setUserNum(i % 5);
			post.setPostViewCnt((int) (Math.random() * 100));
			post.setPostPostedDate(LocalDateTime.now());
			allPosts.add(post);
		}

		// 2. 페이징 계산
		int limit = 10; // 한 페이지에 보여줄 글 개수
		int totalPosts = allPosts.size(); // 총 105개
		//검색한 키워드가 없는경우 처리(없으면 1페이지)
		int totalPages = totalPosts == 0 ? 1 : (int) Math.ceil((double) totalPosts / limit); // 총 11페이지

		// 리스트 자르기 (subList)
		int startIdx = (page - 1) * limit;
		int endIdx = Math.min(startIdx + limit, totalPosts);
		//총 검색된 게시글의 수가 최대페이징 첫게시글이(38개 조회됐으면 31) 같거나작다? => 아무것도 조회디지 않았다
		List<PostDTO> pagedList = startIdx >= totalPosts ? new ArrayList<>() : allPosts.subList(startIdx, endIdx);

		// 하단 페이징 블록 계산 (1~5, 6~10)
		int blockSize = 5;
		int startPage = ((page - 1) / blockSize) * blockSize + 1;
		int endPage = Math.min(startPage + blockSize - 1, totalPages);

		// 3. 자른 목록과 페이징 정보를 Map에 담아서 반환
		PageResultDTO<PostDTO> resultPage = new PageResultDTO<>(pagedList, // 1. list (10개의 데이터)
				page, // 2. currentPage (현재 페이지)
				totalPages, // 3. totalPages (총 페이지)
				startPage, // 4. startPage (시작 페이지)
				endPage, // 5. endPage (끝 페이지)
				allPosts.size() // 6. totalCount (총 댓글 개수)
		);

		//********테스트용 데이터**********
		return resultPage;
	}

	@Override
	public PostDTO getPost(int postNum) {

		//postNum을 키값으로 테이블 조회해서 게시글 정보 가져오기 
		//=========테스트데이터==============
		PostDTO post = new PostDTO();
		post.setPostNum((long) 3);
		post.setPostTitle("testTitle");
		post.setPostCategoryNum(2);
		post.setPostContent("testContent");
		post.setUserName("test개미");
		post.setPostLikeCnt(552);
		post.setPostViewCnt(123);
		post.setPostPostedDate(LocalDateTime.now());
		post.setPostFiles("3_1.jpg,3_2.png");
		//=========테스트데이터==============
		
		return post;
	}

	@Override
	public List<PostDTO> getPostListPagingSortedByView(int pageIdx, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

}
