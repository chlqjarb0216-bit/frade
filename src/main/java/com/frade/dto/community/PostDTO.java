package com.frade.dto.community;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PostDTO {

	private Long postNum;             // p_num : 게시글 번호 (PK)
    private Long userNum;             // u_num : 회원 번호 (작성자 FK)
    private String userName;		   // u_num 을 통해 유저 이름을 저장
    private Integer postCategoryNum;  // p_category_num : 카테고리 번호(0:질문 1:정보 2:자유)
//    private Integer sectorNum;         // sc_num : 종목/세부 카테고리 번호(반도체, 의료 등 코드번호)
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 30, message = "제목을 최대 30자 이내로 작성해주세요.")
    private String postTitle;         // p_title : 게시글 제목
    
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 1333, message = "내용이 너무 깁니다. (최대 1333자).")
    private String postContent;       // p_content : 게시글 본문
    private Integer postViewCnt;      // p_view_cnt : 조회수
    private Integer postLikeCnt;      // p_like_cnt : 추천수(좋아요)
    private LocalDateTime postPostedDate;      // p_posted_date : 작성일자
    
//    private Date postUpdatedDate;     // p_updated_date : 수정일자
//    private Long postTrNum1;          // p_tr_num1 : 관련 거래/참조 번호 1
//    private Long postTrNum2;          // p_tr_num2 : 관련 거래/참조 번호 2
//    private Long postTrNum3;          // p_tr_num3 : 관련 거래/참조 번호 3
    private String postFiles;         // p_files : 첨부파일 경로 또는 식별자
//    private Integer postIsPublic;     // p_is_public : 공개 여부 (예: 1=공개, 0=비공개)
    
    
    
    
    public String getPostedDateString() {
        if (this.postPostedDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return this.postPostedDate.format(formatter);
    }
    

}
