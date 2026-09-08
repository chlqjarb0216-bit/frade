package com.frade.dao.stock.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.stock.StockDAO;
import com.frade.dto.stock.StockInfoDTO;

@Repository
public class StockDAOImpl implements StockDAO {

	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<StockInfoDTO> selectAllStock() {
		return sqlSessionTemplate.selectList("stock_mapper.findAllStockInfo");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> updateAllStockAndReturnMatchedCodeList(String infoListXml) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("xmlData", infoListXml);
		paramMap.put("resultList", null); // 오라클이 채워줄 빈자리

		sqlSessionTemplate.update("stock_mapper.updateAllStockAndReturnMatchedCodes", paramMap);

		return (List<String>) paramMap.get("resultList");
	}

}
