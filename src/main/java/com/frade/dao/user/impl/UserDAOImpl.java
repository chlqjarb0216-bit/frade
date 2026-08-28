package com.frade.dao.user.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.user.UserDAO;
import com.frade.dto.user.UserSignDTO;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;

    @Override
    public UserSignDTO findUserById(String uId) {

        UserSignDTO user = sqlSessionTemplate.selectOne( "userMapper.findUserById", uId);

        return user;
    }
}
