package com.frade.dto.community;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class CommentDTO {
	private Integer commentNum;
	private Integer postNum;
	private Integer userNum;
	
	private String userName;	//유저번호를 통해 유저 이름 담을 변수
	
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
