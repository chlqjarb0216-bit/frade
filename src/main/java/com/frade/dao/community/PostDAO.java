package com.frade.dao.community;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PostDAO {
	
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
	
}
