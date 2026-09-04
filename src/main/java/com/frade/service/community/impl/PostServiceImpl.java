package com.frade.service.community.impl;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

		// 게시글 시퀀스 먼저 따오기
		long nextNum = postDAO.getNextPostNum();

		post.setPostNum(nextNum); // DTO에 번호 세팅

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
		int limit = 10; // 한 페이지에 보여줄 글 개수
		int offset = (page - 1) * limit; // 건너뛸 데이터 개수 (DB용)

		//  MyBatis(SqlSessionTemplate)로 넘길 파라미터들을 Map에 포장
		Map<String, Object> params = new HashMap<>();
		params.put("keyword", keyword);
		params.put("type", type);
		params.put("offset", offset);
		params.put("limit", limit);

		// DB에서 실제 전체 게시글 수 가져오기 (검색 조건 반영)
		int totalPosts = postDAO.selectPostTotalCount(params);

		//  DB에서 페이징 처리된 실제 게시글 리스트 가져오기
		List<PostDTO> pagedList = new ArrayList<>();
		if (totalPosts > 0) {
			pagedList = postDAO.selectPostList(params);
		}

		//  페이징 로직 계산 
		int totalPages = totalPosts == 0 ? 1 : (int) Math.ceil((double) totalPosts / limit);
		int blockSize = 5;
		int startPage = ((page - 1) / blockSize) * blockSize + 1;
		int endPage = Math.min(startPage + blockSize - 1, totalPages);

		//  PageResultDTO 객체 반환
		return new PageResultDTO<>(
				pagedList, 
				page, 
				totalPages, 
				startPage, 
				endPage, 
				totalPosts
		);
	}

	@Override
	public PostDTO getPost(int postNum, boolean isViewUp) {

		// 컨트롤러에서 true라고 할 때만 조회수 증가
	    if (isViewUp) {
	        postDAO.updateViewCount(postNum);
	    }

		// DB에서 실제 게시글 상세 정보 가져와서 그대로 리턴
		return postDAO.selectPost(postNum);

	}

	@Override
	public List<PostDTO> getPostListPagingSortedByView(int pageIdx, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

}
