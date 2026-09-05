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
		int totalPosts = postDAO.getPostTotalCount(params);

		//  DB에서 페이징 처리된 실제 게시글 리스트 가져오기
		List<PostDTO> pagedList = new ArrayList<>();
		if (totalPosts > 0 && offset < totalPosts) {
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
	public List<PostDTO> getPostListPagingSortedByView(int pageSize) {
		
		List<PostDTO> mainPosts = postDAO.selectMainPosts(pageSize);
		return mainPosts;
	}

	@Override
	public int deletePost(int postNum) {

		PostDTO post = postDAO.selectPost(postNum);
		
		int result = postDAO.deletePost(postNum);
		
		if(result> 0 && post != null && post.getPostFiles() != null && !post.getPostFiles().isEmpty()) {
			String baseDir = FilePath.FILE_ABSOLUTE_STORE_PATH + FilePath.POST_UPLOADFILE_PATH;
		
		for (String fileName : post.getFileList()) {
            File targetFile = new File(baseDir, fileName);
            
            if (targetFile.exists()) {
                	targetFile.delete(); // 실제 D드라이브에서 파일 싹둑!
                	log.info("첨부파일 삭제 완료: " + fileName);
            	}
        	}
		}
		return result;
	}

	@Override
	public int updatePost(PostDTO post, MultipartFile[] files, boolean deleteExistingFiles) {
		// 1. 기존 게시글 조회
		PostDTO existingPost = postDAO.selectPost(post.getPostNum().intValue());

		String baseDir = FilePath.FILE_ABSOLUTE_STORE_PATH + FilePath.POST_UPLOADFILE_PATH;
		File folder = new File(baseDir);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		// 2. 새 파일 첨부 여부 확인
		boolean hasNewFiles = false;
		if (files != null && files.length > 0) {
			for (MultipartFile file : files) {
				if (!file.isEmpty()) {
					hasNewFiles = true;
					break;
				}
			}
		}

		// 3. 파일 처리
		// 3-1. "기존 첨부파일 삭제" 체크박스가 선택된 경우 -> 기존 파일 삭제
		if (deleteExistingFiles) {
			if (existingPost.getPostFiles() != null && !existingPost.getPostFiles().isEmpty()) {
				for (String fileName : existingPost.getFileList()) {
					File targetFile = new File(baseDir, fileName);
					if (targetFile.exists()) {
						targetFile.delete();
						log.info("기존 첨부파일 삭제 완료: " + fileName);
					}
				}
			}

			// 기존 파일 삭제 후 새 파일이 업로드된 경우 -> 새 파일만 저장
			if (hasNewFiles) {
				StringJoiner sj = new StringJoiner(",");
				int fileIndex = 1;
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						String originalName = file.getOriginalFilename();
						String ext = originalName.substring(originalName.lastIndexOf("."));
						String savedFileName = post.getPostNum() + "_" + fileIndex + ext;
						sj.add(savedFileName);

						File saveFile = new File(baseDir, savedFileName);
						try {
							file.transferTo(saveFile);
						} catch (IllegalStateException | IOException e) {
							e.printStackTrace();
							log.warn(e.getMessage());
						}
						fileIndex++;
					}
				}
				post.setPostFiles(sj.toString());
			} else {
				// 새 파일도 없으면 파일 없음(null)
				post.setPostFiles(null);
			}

		} else {
			// 3-2. 기존 파일 삭제를 체크하지 않은 경우 -> 기존 파일 보존!
			List<String> preservedFiles = new ArrayList<>();
			if (existingPost.getPostFiles() != null && !existingPost.getPostFiles().isEmpty()) {
				preservedFiles.addAll(existingPost.getFileList());
			}

			if (hasNewFiles) {
				// 기존 파일들을 StringJoiner에 먼저 담음
				StringJoiner sj = new StringJoiner(",");
				for (String prevFile : preservedFiles) {
					sj.add(prevFile);
				}

				// 기존 파일 목록 뒤에 새 파일 추가 (파일명 중복 방지)
				int fileIndex = preservedFiles.size() + 1;
				for (MultipartFile file : files) {
					if (!file.isEmpty()) {
						String originalName = file.getOriginalFilename();
						String ext = originalName.substring(originalName.lastIndexOf("."));

						String savedFileName = post.getPostNum() + "_" + fileIndex + ext;
						File saveFile = new File(baseDir, savedFileName);
						while (saveFile.exists()) {
							fileIndex++;
							savedFileName = post.getPostNum() + "_" + fileIndex + ext;
							saveFile = new File(baseDir, savedFileName);
						}

						sj.add(savedFileName);

						try {
							file.transferTo(saveFile);
						} catch (IllegalStateException | IOException e) {
							e.printStackTrace();
							log.warn(e.getMessage());
						}
						fileIndex++;
					}
				}
				post.setPostFiles(sj.toString());
			} else {
				// 새 파일도 없으면 기존 파일 목록 그대로 유지
				post.setPostFiles(existingPost.getPostFiles());
			}
		}

		// 4. DB UPDATE 실행
		return postDAO.updatePost(post);
	}

}
