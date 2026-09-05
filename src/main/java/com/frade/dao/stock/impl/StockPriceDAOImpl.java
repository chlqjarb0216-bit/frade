package com.frade.dao.stock.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.stock.StockPriceDAO;
import com.frade.dto.stock.StockPriceDTO;

@Repository
public class StockPriceDAOImpl implements StockPriceDAO {

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int insertMinuteStockPrice(List<StockPriceDTO> stockPriceList) {
		return sqlSessionTemplate.insert("stockprice_mapper.insertMinuteStockPrice", stockPriceList);
	}

}
