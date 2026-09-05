package com.frade.dao.user;

import com.frade.dto.user.UserDTO;

public interface UserDAO {

    // 아이디 중복 확인
    int countUserId(String userId);

    // 닉네임 중복 확인
    int countUserNick(String userNick);

    // 이메일 중복 확인
    int countUserEmail(String userEmail);

    // 회원가입에 사용할 다음 user_num 조회
    int getNextUserNum();

    // 회원정보 저장
    int saveUser(UserDTO userDTO);

    // 회원 지갑 생성
    int saveUserCash(int userNum);
}