package com.frade.dao.user.impl;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.frade.dao.user.UserDAO;
import com.frade.dto.user.UserDTO;

@Repository
public class UserDAOImpl implements UserDAO {

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;


    // 아이디 중복 확인
    // 같은 아이디를 가진 회원 수를 반환
    // 0 : 중복 없음
    // 1 이상 : 중복 있음
    @Override
    public Integer findUserNumById(String userId) {

        return sqlSessionTemplate.selectOne(
                "user_mapper.findUserNumById",
                userId
        );
    }


    // 닉네임 중복 확인
    // 같은 닉네임을 가진 회원 수를 반환
    // 0 : 중복 없음
    // 1 이상 : 중복 있음
    @Override
    public Integer findUserNumByNick(String userNick) {

        return sqlSessionTemplate.selectOne(
                "user_mapper.findUserNumByNick",
                userNick
        );
    }


    // 이메일 중복 확인
    // 같은 이메일을 가진 회원 수를 반환
    // 0 : 중복 없음
    // 1 이상 : 중복 있음
    @Override
    public Integer findUserNumByEmail(String userEmail) {

        return sqlSessionTemplate.selectOne(
                "user_mapper.findUserNumByEmail",
                userEmail
        );
    }


    // 회원가입에 사용할 새로운 user_num 조회
    // Oracle SEQ_T_USER 시퀀스의 NEXTVAL 값을 가져옴
    // 가져온 번호는 T_USER와 T_CASH에서 동일하게 사용
    @Override
    public int getNextUserNum() {

        return sqlSessionTemplate.selectOne(
                "user_mapper.getNextUserNum"
        );
    }


    // T_USER 테이블에 회원정보 저장
    // Service에서 시퀀스로 만든 userNum이 들어있는
    // UserDTO를 전달받아 INSERT
    @Override
    public int saveUser(UserDTO userDTO) {

        return sqlSessionTemplate.insert(
                "user_mapper.saveUser",
                userDTO
        );
    }


    // 회원가입한 유저의 T_CASH 데이터 생성
    // T_USER에 사용한 것과 동일한 userNum을 전달
    // CASH와 MARGIN은 DB의 DEFAULT 값 사용
    @Override
    public int saveUserCash(int userNum) {

        return sqlSessionTemplate.insert(
                "user_mapper.saveUserCash",
                userNum
        );
    }


	@Override
	public UserDTO findUserById(String userId) {
		
		// 아이디로 로그인에 필요한 회원정보 조회
	    return sqlSessionTemplate.selectOne(
	            "user_mapper.findUserById",
	            userId
	    );
	}
}