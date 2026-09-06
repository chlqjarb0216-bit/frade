package com.frade.dao.stock.impl;

import java.util.List;
import java.util.Map;

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

	@Override
	public List<StockPriceDTO> selectMinuteStockPriceListByStockCodeAndDayString(String stockCode, String dayString) {
		Map<String, String> paramMap = Map.of("stockCode", stockCode, "dayString", dayString);
		return sqlSessionTemplate.selectList("stockprice_mapper.selectMinuteStockPriceListByStockCodeAndDayString",
				paramMap);
	}

}
