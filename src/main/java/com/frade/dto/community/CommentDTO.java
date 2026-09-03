package com.frade.dto.community;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CommentDTO {
	private Long commentNum;
	private Long postNum;
	private int userNum;
	
	private String userName;	//유저번호를 통해 유저 이름 담을 변수
	
	@NotBlank(message = "댓글 내용을 입력해주세요.")
	@Size(max = 100, message = "댓글을 최대 100까지만 작성할 수 있습니다")
	private String commentContent;	//
	
    private LocalDateTime postCommentedDate;
	
    
    //DB에 시간 데이터를 JSON에 넘겨줄때 String 타입으로 변환 시켜주는 getter
    public String getCommentedDateString() {
        if (this.postCommentedDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return this.postCommentedDate.format(formatter);
    }
	
}
