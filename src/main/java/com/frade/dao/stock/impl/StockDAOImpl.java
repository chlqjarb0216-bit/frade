package com.frade.dao.stock.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.frade.dao.stock.StockDAO;
import com.frade.dto.stock.StockInfoDTO;

@Repository
public class StockDAOImpl implements StockDAO {

	@Override
	public List<StockInfoDTO> selectAllStock() {
		// TODO Auto-generated method stub
		return new ArrayList<StockInfoDTO>();
	}

}
