SET DEFINE OFF;


CREATE TABLE t_sector (
  sector_num NUMBER(2),
  sector_name VARCHAR2(100) NOT NULL,
  CONSTRAINT pk_t_sector PRIMARY KEY (sector_num)
);

INSERT ALL
  INTO t_sector (sector_num, sector_name) VALUES (1, '화학')
  INTO t_sector (sector_num, sector_name) VALUES (2, '기타금융')
  INTO t_sector (sector_num, sector_name) VALUES (3, '전기·전자')
  INTO t_sector (sector_num, sector_name) VALUES (4, '유통')
  INTO t_sector (sector_num, sector_name) VALUES (5, '운송장비·부품')
  INTO t_sector (sector_num, sector_name) VALUES (6, '금속')
  INTO t_sector (sector_num, sector_name) VALUES (7, '제약')
  INTO t_sector (sector_num, sector_name) VALUES (8, '음식료·담배')
  INTO t_sector (sector_num, sector_name) VALUES (9, '건설')
  INTO t_sector (sector_num, sector_name) VALUES (10, '일반서비스')
  INTO t_sector (sector_num, sector_name) VALUES (11, '기계·장비')
  INTO t_sector (sector_num, sector_name) VALUES (12, '증권')
  INTO t_sector (sector_num, sector_name) VALUES (13, '섬유·의류')
  INTO t_sector (sector_num, sector_name) VALUES (14, '운송·창고')
  INTO t_sector (sector_num, sector_name) VALUES (15, 'IT 서비스')
  INTO t_sector (sector_num, sector_name) VALUES (16, '부동산')
  INTO t_sector (sector_num, sector_name) VALUES (17, '비금속')
  INTO t_sector (sector_num, sector_name) VALUES (18, '종이·목재')
  INTO t_sector (sector_num, sector_name) VALUES (19, '보험')
  INTO t_sector (sector_num, sector_name) VALUES (20, '오락·문화')
  INTO t_sector (sector_num, sector_name) VALUES (21, '전기·가스')
  INTO t_sector (sector_num, sector_name) VALUES (22, '기타제조')
  INTO t_sector (sector_num, sector_name) VALUES (23, '의료·정밀기기')
  INTO t_sector (sector_num, sector_name) VALUES (24, '통신')
  INTO t_sector (sector_num, sector_name) VALUES (25, '은행')
  INTO t_sector (sector_num, sector_name) VALUES (26, '농업, 임업 및 어업')
  INTO t_sector (sector_num, sector_name) VALUES (99, '임시분류')
SELECT * FROM DUAL;


CREATE SEQUENCE seq_t_user START WITH 1 INCREMENT BY 1;

CREATE TABLE t_user (
  user_num NUMBER(8) DEFAULT seq_t_user.NEXTVAL,
  user_id VARCHAR2(16) NOT NULL,
  user_nick VARCHAR2(48) NOT NULL,
  user_email VARCHAR2(320) NOT NULL,
  user_pw VARCHAR2(100) NOT NULL,
  user_registed_date DATE DEFAULT SYSDATE NOT NULL,
  user_portfolio_is_public NUMBER(1) DEFAULT 0 NOT NULL,
  user_photo VARCHAR2(13),
  user_is_deleted NUMBER(1),
  CONSTRAINT pk_t_user PRIMARY KEY (user_num)
);


CREATE TABLE t_cash (
  user_num NUMBER(8),
  cash NUMBER DEFAULT 10000000 NOT NULL,
  CONSTRAINT pk_t_cash PRIMARY KEY (user_num)
);


CREATE TABLE t_category (
  post_category_num NUMBER(1),
  post_category_name VARCHAR2(30) NOT NULL,
  CONSTRAINT pk_t_category PRIMARY KEY (post_category_num)
);

INSERT ALL
  INTO t_category (post_category_num, post_category_name) VALUES (1, '자유')
  INTO t_category (post_category_num, post_category_name) VALUES (2, '정보')
  INTO t_category (post_category_num, post_category_name) VALUES (3, '질문')
SELECT * FROM DUAL;


CREATE TABLE t_stock (
  stock_code VARCHAR2(6),
  stock_name VARCHAR2(60) NOT NULL,
  sector_num NUMBER(2) NOT NULL,
  stock_status NUMBER(1) NOT NULL,
  CONSTRAINT pk_t_stock PRIMARY KEY (stock_code)
);

INSERT ALL
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('005930','삼성전자',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('000660','SK하이닉스',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('005935','삼성전자우',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('402340','SK스퀘어',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('009150','삼성전기',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('005380','현대차',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('373220','LG에너지솔루션',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('207940','삼성바이오로직스',7,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('028260','삼성물산',4,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('105560','KB금융',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('032830','삼성생명',19,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('012450','한화에어로스페이스',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('000270','기아',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('034020','두산에너빌리티',11,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('055550','신한지주',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('329180','HD현대중공업',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('012330','현대모비스',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('068270','셀트리온',7,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('006400','삼성SDI',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('034730','SK',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('086790','하나금융지주',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('035420','NAVER',15,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('066570','LG전자',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('010130','고려아연',6,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('010120','LS ELECTRIC',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('000810','삼성화재',19,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('042660','한화오션',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('005490','POsectorO홀딩스',6,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('267260','HD현대일렉트릭',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('298040','효성중공업',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('009540','HD한국조선해양',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('316140','우리금융지주',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('011200','HMM',14,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('017670','SK텔레콤',24,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('096770','SK이노베이션',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('015760','한국전력',21,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('042700','한미반도체',11,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('138040','메리츠금융지주',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('006800','미래에셋증권',12,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('051910','LG화학',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('033780','KT&G',8,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('010140','삼성중공업',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('000150','두산',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('018260','삼성에스디에스',15,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('003550','LG',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('267250','HD현대',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('278470','에이피알',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('024110','기업은행',25,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('086280','현대글로비스',14,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('035720','카카오',15,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('079550','LIG디펜스앤에어로스페이스',6,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('010950','S-Oil',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('003670','포스코퓨처엠',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('064350','현대로템',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('011070','LG이노텍',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('272210','한화시스템',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('000720','현대건설',9,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('030200','KT',24,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('047810','한국항공우주',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('307950','현대오토에버',15,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('005830','DB손해보험',19,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('078930','GS',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('003230','삼양식품',8,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('071050','한국금융지주',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('323410','카카오뱅크',25,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('259960','크래프톤',15,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('005940','NH투자증권',12,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('003490','대한항공',14,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('047050','포스코인터내셔널',4,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('443060','HD현대마린솔루션',10,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('006260','LS',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('0126Z0','삼성에피스홀딩스',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('028050','삼성E&A',10,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('090430','아모레퍼시픽',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('161390','한국타이어앤테크놀로지',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('180640','한진칼',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('016360','삼성증권',12,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('007660','이수페타시스',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('352820','하이브',20,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('009830','한화솔루션',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('047040','대우건설',9,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('039490','키움증권',12,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('064400','LG씨엔에스',15,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('005387','현대차2우B',5,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('021240','코웨이',10,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('000100','유한양행',7,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('326030','SK바이오팜',10,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('267270','HD건설기계',11,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('128940','한미약품',7,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('032640','LG유플러스',24,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('000880','한화',1,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('377300','카카오페이',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('241560','두산밥캣',11,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('353200','대덕전자',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('001440','대한전선',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('029780','삼성카드',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('062040','산일전기',3,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('010060','OCI홀딩스',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('175330','JB금융지주',2,0)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status) VALUES ('271560','오리온',8,0)
SELECT * FROM DUAL;


CREATE SEQUENCE seq_t_post START WITH 1 INCREMENT BY 1;

CREATE TABLE t_post (
  post_num NUMBER DEFAULT seq_t_post.NEXTVAL,
  user_num NUMBER(8) NOT NULL,
  post_category_num NUMBER(1) NOT NULL,
  sector_num NUMBER(2),
  post_title VARCHAR2(90) NOT NULL,
  post_content VARCHAR2(4000) NOT NULL,
  post_view_cnt NUMBER(9) DEFAULT 0 NOT NULL,
  post_like_cnt NUMBER(8) DEFAULT 0 NOT NULL,
  post_posted_date DATE DEFAULT SYSDATE NOT NULL,
  post_updated_date DATE,
  post_trade_num1 NUMBER,
  post_trade_num2 NUMBER,
  post_trade_num3 NUMBER,
  post_files VARCHAR2(100),
  post_is_public NUMBER(1) NOT NULL,
  CONSTRAINT pk_t_post PRIMARY KEY (post_num)
);


CREATE TABLE t_stock_price (
  stock_code VARCHAR2(6),
  date_time DATE,
  price_open NUMBER(8) NOT NULL,
  price_high NUMBER(8) NOT NULL,
  price_low NUMBER(8) NOT NULL,
  price_close NUMBER(8) NOT NULL,
  volume NUMBER NOT NULL,
  CONSTRAINT pk_t_stock_price PRIMARY KEY (stock_code, date_time)
);


CREATE TABLE t_portfolio (
  user_num NUMBER(8),
  stock_code VARCHAR2(6) NOT NULL,
  user_stock_cnt NUMBER NOT NULL,
  user_buy_cost NUMBER NOT NULL,
  CONSTRAINT pk_t_portfolio PRIMARY KEY (user_num, stock_code)
);


CREATE SEQUENCE seq_t_history START WITH 1 INCREMENT BY 1;

CREATE TABLE t_history (
  trade_num NUMBER DEFAULT seq_t_history.NEXTVAL,
  user_num NUMBER(8) NOT NULL,
  stock_code VARCHAR2(6) NOT NULL,
  trade_price NUMBER NOT NULL,
  trade_cnt NUMBER NOT NULL,
  trade_date DATE DEFAULT SYSDATE NOT NULL,
  CONSTRAINT pk_t_history PRIMARY KEY (trade_num)
);


CREATE SEQUENCE seq_t_comment START WITH 1 INCREMENT BY 1;

CREATE TABLE t_comment (
  comment_num NUMBER DEFAULT seq_t_comment.NEXTVAL,
  post_num NUMBER NOT NULL,
  user_num NUMBER(8) NOT NULL,
  comment_content VARCHAR2(90) NOT NULL,
  comment_date DATE DEFAULT SYSDATE NOT NULL,
  comment_updated_date DATE,
  CONSTRAINT pk_t_comment PRIMARY KEY (comment_num)
);


CREATE UNIQUE INDEX uq_t_user_id ON t_user (user_id);

CREATE UNIQUE INDEX uq_t_user_nick ON t_user (user_nick);

CREATE UNIQUE INDEX uq_t_user_email ON t_user (user_email);

CREATE INDEX idx_t_stock_sector ON t_stock (sector_num);

CREATE INDEX idx_t_post_user_date ON t_post (user_num, post_posted_date);

CREATE INDEX idx_t_post_category_date ON t_post (post_category_num, post_posted_date);

CREATE INDEX idx_t_post_sector_date ON t_post (sector_num, post_posted_date);

CREATE INDEX idx_t_history_user_date ON t_history (user_num, trade_date);

CREATE INDEX idx_t_comment_postnum_date ON t_comment (post_num, comment_date);

CREATE INDEX idx_t_comment_user_date ON t_comment (user_num, comment_date);


ALTER TABLE t_stock ADD CONSTRAINT fk_t_stock_to_t_sector FOREIGN KEY (sector_num) REFERENCES t_sector (sector_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_cash ADD CONSTRAINT fk_t_cash_to_t_user FOREIGN KEY (user_num) REFERENCES t_user (user_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_post ADD CONSTRAINT fk_t_post_to_t_user FOREIGN KEY (user_num) REFERENCES t_user (user_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_post ADD CONSTRAINT fk_t_post_to_t_category FOREIGN KEY (post_category_num) REFERENCES t_category (post_category_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_post ADD CONSTRAINT fk_t_post_to_t_sector FOREIGN KEY (sector_num) REFERENCES t_sector (sector_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_post ADD CONSTRAINT fk_t_post_to_t_history1 FOREIGN KEY (post_trade_num1) REFERENCES t_history (trade_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_post ADD CONSTRAINT fk_t_post_to_t_history2 FOREIGN KEY (post_trade_num2) REFERENCES t_history (trade_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_post ADD CONSTRAINT fk_t_post_to_t_history3 FOREIGN KEY (post_trade_num3) REFERENCES t_history (trade_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_stock_price ADD CONSTRAINT fk_t_stock_price_to_t_stock FOREIGN KEY (stock_code) REFERENCES t_stock (stock_code) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_portfolio ADD CONSTRAINT fk_t_portfolio_to_t_user FOREIGN KEY (user_num) REFERENCES t_user (user_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_portfolio ADD CONSTRAINT fk_t_portfolio_to_t_stock FOREIGN KEY (stock_code) REFERENCES t_stock (stock_code) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_history ADD CONSTRAINT fk_t_history_to_t_user FOREIGN KEY (user_num) REFERENCES t_user (user_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_history ADD CONSTRAINT fk_t_history_to_t_stock FOREIGN KEY (stock_code) REFERENCES t_stock (stock_code) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_comment ADD CONSTRAINT fk_t_comment_to_t_post FOREIGN KEY (post_num) REFERENCES t_post (post_num) DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE t_comment ADD CONSTRAINT fk_t_comment_to_t_user FOREIGN KEY (user_num) REFERENCES t_user (user_num) DEFERRABLE INITIALLY IMMEDIATE;


commit;