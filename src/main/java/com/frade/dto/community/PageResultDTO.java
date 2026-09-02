package com.frade.dto.community;

import java.util.List;

import lombok.Data;

@Data
public class PageResultDTO<T> {
	
	private List<T> list;       // 현재 페이지의 데이터 목록 (게시글 리스트, 댓글 리스트 등..)
    private int currentPage;    // 현재 페이지 번호
    private int totalPages;     // 전체 페이지 수
    private int startPage;      // 하단 페이징 시작 번호
    private int endPage;        // 하단 페이징 끝 번호
    private int totalCount;     // 전체 데이터(게시글/댓글) 총 개수
    
	public PageResultDTO(List<T> list, int currentPage, int totalPages, int startPage, int endPage, int totalCount) {
		super();
		this.list = list;
		this.currentPage = currentPage;
		this.totalPages = totalPages;
		this.startPage = startPage;
		this.endPage = endPage;
		this.totalCount = totalCount;
	}
}
