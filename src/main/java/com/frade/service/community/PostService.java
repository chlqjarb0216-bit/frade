package com.frade.service.community;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.frade.dto.community.PostDTO;

public interface PostService {
	
	//작성한 게시글 db에 저장
	public int savePost(PostDTO post, MultipartFile[] files);

	//게시글 목록 데이터 가져오기
    // 1.페이징 수학 계산
    // 2.리스트 자르기 (subList)
    // 3.하단 페이징 블록 계산 (1~5, 6~10)
	// 4.자른 목록과 페이징 정보를 Map에 담아서 반환
	// ++)추가로 uNum가지고 테이블 조인시켜서 작성자 이름 혹은 닉네임 가져와야함
	public Map<String, Object> getPostList(int page, String keyword, int type);
	
	
	public PostDTO getPost(int postNum);
	
	
}
