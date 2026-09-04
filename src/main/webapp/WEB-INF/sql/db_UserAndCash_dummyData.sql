-- T_USER와 T_CASH 테이블 USER_NUM 1로 더미데이터 생성용 쿼리

INSERT INTO T_USER (
    USER_NUM,
    USER_ID,
    USER_NICK,
    USER_EMAIL,
    USER_PW,
    USER_REGISTED_DATE,
    USER_PORTFOLIO_IS_PUBLIC,
    USER_PHOTO,
    USER_IS_DELETED
) VALUES (
    1,
    'testuser',
    '테스트유저',
    'testuser@test.com',
    'test',
    SYSDATE,
    1,
    NULL,
    0
);

INSERT INTO T_CASH
VALUES(1, 10000000, 0);

COMMIT;