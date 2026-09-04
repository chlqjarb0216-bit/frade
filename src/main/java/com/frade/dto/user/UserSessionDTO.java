package com.frade.dto.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserSessionDTO {

    int userNum;
    String userNick;
    String userPhoto;

    public UserSessionDTO(
            int userNum,
            String userNick,
            String userPhoto) {

        this.userNum = userNum;
        this.userNick = userNick;
        this.userPhoto = userPhoto;
    }
}