package com.frade.service.main;
import java.util.List;
import com.frade.dto.community.PostDTO;
import com.frade.dto.stock.StockPreviewDTO;
/**
 * 메인 대시보드 화면 서비스 인터페이스
 */
public interface MainService {
	/**
	 * 실시간 인기 주식 상위 5개 목록 조회
	 * @return List<StockPreviewDTO>
	 */
	public List<StockPreviewDTO> getTop5Stocks();
	/**
	 * 커뮤니티 인기 게시글 상위 5개 목록 조회
	 * @return List<PostDTO>
	 */
	public List<PostDTO> getTop5Posts();
}