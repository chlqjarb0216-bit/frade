package com.frade.dto.community;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDTO {

	private Long postNum;             // p_num : 게시글 번호 (PK)
    private int userNum;             // u_num : 회원 번호 (작성자 FK)
    private String userName;		   // u_num 을 통해 유저 이름을 저장
    private int postCategoryNum;  // p_category_num : 카테고리 번호 (1:자유, 2:정보, 3:질문)
//    private Integer sectorNum;         // sc_num : 종목/세부 카테고리 번호(반도체, 의료 등 코드번호)
    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 30, message = "제목을 최대 30자 이내로 작성해주세요.")
    private String postTitle;         // p_title : 게시글 제목
    
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 1333, message = "내용이 너무 깁니다. (최대 1333자).")
    private String postContent;       // p_content : 게시글 본문
    private int postViewCnt;      // p_view_cnt : 조회수
    private int postLikeCnt;      // p_like_cnt : 추천수(좋아요)
    private LocalDateTime postPostedDate;      // p_posted_date : 작성일자
    
//    private Date postUpdatedDate;     // p_updated_date : 수정일자
//    private Integer postTrNum1;          // p_tr_num1 : 관련 거래/참조 번호 1
//    private Integer postTrNum2;          // p_tr_num2 : 관련 거래/참조 번호 2
//    private Integer postTrNum3;          // p_tr_num3 : 관련 거래/참조 번호 3
    private String postFiles;         // p_files : 첨부파일 경로 또는 식별자
//    private int postIsPublic = 1;     // p_is_public : 공개 여부 (예: 1=공개, 0=비공개)
    
    
    
    //DB에 시간 데이터를 JSON에 넘겨줄때 String 타입으로 변환 시켜주는 getter
    public String getPostedDateString() {
        if (this.postPostedDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return this.postPostedDate.format(formatter);
    }
    
    //postFiles 쪼개기(화면전달용 Getter)
    public List<String> getFileList() {
        // 첨부파일이 아예 없는 경우 (빈 리스트 반환)
        if (this.postFiles == null || this.postFiles.trim().isEmpty()) {
            return new ArrayList<>(); 
        }
        
        //  쉼표를 기준으로 문자열을 쪼개서 리스트로 변환해 반환
        return Arrays.asList(this.postFiles.split(","));
    }

}
