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
  user_num NUMBER(9) DEFAULT seq_t_user.NEXTVAL, --java int 최대자리 대응
  user_id VARCHAR2(16) NOT NULL,
  user_nick VARCHAR2(48) NOT NULL,
  user_email VARCHAR2(320) NOT NULL,
  user_pw VARCHAR2(100) NOT NULL,
  user_registed_date DATE DEFAULT SYSDATE NOT NULL,
  user_portfolio_is_public NUMBER(1) DEFAULT 0 NOT NULL,
  user_photo VARCHAR2(14), --user_num 자릿수 + 5
  user_is_deleted NUMBER(1),
  CONSTRAINT pk_t_user PRIMARY KEY (user_num)
);


CREATE TABLE t_cash (
  user_num NUMBER(9), --t_user user_num
  cash NUMBER(18) DEFAULT 10000000 NOT NULL, --java long 대응
  margin NUMBER(18) DEFAULT 0 NOT NULL, --증거금
  CONSTRAINT pk_t_cash PRIMARY KEY (user_num),
  CONSTRAINT ck_t_cash_cash CHECK (cash>=0),
  CONSTRAINT ck_t_cash_margin CHECK (margin>=0 AND margin<=cash)
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
  prev_day_close_price NUMBER(9) NOT NULL,
  CONSTRAINT pk_t_stock PRIMARY KEY (stock_code),
  CONSTRAINT ck_t_stock_prev CHECK (prev_day_close_price>0)
);

INSERT ALL
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('005930','삼성전자',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('000660','SK하이닉스',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('005935','삼성전자우',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('402340','SK스퀘어',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('009150','삼성전기',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('005380','현대차',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('373220','LG에너지솔루션',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('207940','삼성바이오로직스',7,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('028260','삼성물산',4,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('105560','KB금융',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('032830','삼성생명',19,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('012450','한화에어로스페이스',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('000270','기아',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('034020','두산에너빌리티',11,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('055550','신한지주',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('329180','HD현대중공업',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('012330','현대모비스',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('068270','셀트리온',7,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('006400','삼성SDI',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('034730','SK',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('086790','하나금융지주',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('035420','NAVER',15,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('066570','LG전자',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('010130','고려아연',6,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('010120','LS ELECTRIC',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('000810','삼성화재',19,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('042660','한화오션',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('005490','POSCO홀딩스',6,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('267260','HD현대일렉트릭',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('298040','효성중공업',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('009540','HD한국조선해양',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('316140','우리금융지주',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('011200','HMM',14,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('017670','SK텔레콤',24,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('096770','SK이노베이션',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('015760','한국전력',21,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('042700','한미반도체',11,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('138040','메리츠금융지주',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('006800','미래에셋증권',12,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('051910','LG화학',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('033780','KT&G',8,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('010140','삼성중공업',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('000150','두산',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('018260','삼성에스디에스',15,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('003550','LG',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('267250','HD현대',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('278470','에이피알',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('024110','기업은행',25,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('086280','현대글로비스',14,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('035720','카카오',15,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('079550','LIG디펜스앤에어로스페이스',6,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('010950','S-Oil',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('003670','포스코퓨처엠',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('064350','현대로템',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('011070','LG이노텍',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('272210','한화시스템',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('000720','현대건설',9,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('030200','KT',24,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('047810','한국항공우주',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('307950','현대오토에버',15,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('005830','DB손해보험',19,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('078930','GS',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('003230','삼양식품',8,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('071050','한국금융지주',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('323410','카카오뱅크',25,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('259960','크래프톤',15,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('005940','NH투자증권',12,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('003490','대한항공',14,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('047050','포스코인터내셔널',4,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('443060','HD현대마린솔루션',10,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('006260','LS',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('0126Z0','삼성에피스홀딩스',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('028050','삼성E&A',10,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('090430','아모레퍼시픽',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('161390','한국타이어앤테크놀로지',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('180640','한진칼',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('016360','삼성증권',12,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('007660','이수페타시스',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('352820','하이브',20,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('009830','한화솔루션',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('047040','대우건설',9,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('039490','키움증권',12,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('064400','LG씨엔에스',15,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('005387','현대차2우B',5,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('021240','코웨이',10,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('000100','유한양행',7,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('326030','SK바이오팜',10,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('267270','HD건설기계',11,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('128940','한미약품',7,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('032640','LG유플러스',24,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('000880','한화',1,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('377300','카카오페이',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('241560','두산밥캣',11,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('353200','대덕전자',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('001440','대한전선',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('029780','삼성카드',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('062040','산일전기',3,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('010060','OCI홀딩스',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('175330','JB금융지주',2,0,50000)
  INTO t_stock (stock_code, stock_name, sector_num, stock_status, prev_day_close_price) VALUES ('271560','오리온',8,0,50000)
SELECT * FROM DUAL;


CREATE SEQUENCE seq_t_post START WITH 1 INCREMENT BY 1;

CREATE TABLE t_post (
  post_num NUMBER(18) DEFAULT seq_t_post.NEXTVAL, --java long 대응
  user_num NUMBER(9) NOT NULL, --t_user user_num
  post_category_num NUMBER(1) NOT NULL,
  sector_num NUMBER(2),
  post_title VARCHAR2(90) NOT NULL,
  post_content VARCHAR2(4000) NOT NULL,
  post_view_cnt NUMBER(9) DEFAULT 0 NOT NULL,
  post_like_cnt NUMBER(8) DEFAULT 0 NOT NULL, --어느유저가 좋아요 눌렀는지 체크할 방법이?
  post_posted_date DATE DEFAULT SYSDATE NOT NULL,
  post_updated_date DATE,
  post_trade_num1 NUMBER(18), --t_history trade_num
  post_trade_num2 NUMBER(18), --t_history trade_num
  post_trade_num3 NUMBER(18), --t_history trade_num
  post_files VARCHAR2(100),
  post_is_public NUMBER(1) DEFAULT 1 NOT NULL,
  CONSTRAINT pk_t_post PRIMARY KEY (post_num)
);


CREATE TABLE t_stock_price (
  stock_code VARCHAR2(6),
  date_time DATE DEFAULT SYSDATE NOT NULL,
  price_open NUMBER(9) NOT NULL, --java int 최대자리 대응
  price_high NUMBER(9) NOT NULL, --java int 최대자리 대응
  price_low NUMBER(9) NOT NULL, --java int 최대자리 대응
  price_close NUMBER(9) NOT NULL, --java int 최대자리 대응
  volume_buy NUMBER(18) DEFAULT 0 NOT NULL, --java long 대응
  volume_sell NUMBER(18) DEFAULT 0 NOT NULL, --java long 대응
  CONSTRAINT pk_t_stock_price PRIMARY KEY (stock_code, date_time),
  CONSTRAINT ck_t_stock_price_open CHECK (price_open>0),
  CONSTRAINT ck_t_stock_price_high CHECK (price_high>0),
  CONSTRAINT ck_t_stock_price_low CHECK (price_low>0),
  CONSTRAINT ck_t_stock_price_close CHECK (price_close>0),
  CONSTRAINT ck_t_stock_volume_buy CHECK (volume_buy>=0),
  CONSTRAINT ck_t_stock_volume_sell CHECK (volume_sell>=0)
);


CREATE TABLE t_portfolio (
  user_num NUMBER(9), --t_user user_num
  stock_code VARCHAR2(6) NOT NULL,
  user_stock_cnt NUMBER(9) NOT NULL, --java int 최대자리 대응
  user_buy_cost NUMBER(18) NOT NULL, --java long 대응
  CONSTRAINT pk_t_portfolio PRIMARY KEY (user_num, stock_code),
  CONSTRAINT ck_t_portfolio_user_stock_cnt CHECK (user_stock_cnt>=0),
  CONSTRAINT ck_t_portfolio_user_buy_cost CHECK (user_buy_cost>=0)
);


CREATE SEQUENCE seq_t_history START WITH 1 INCREMENT BY 1;

CREATE TABLE t_history (
  trade_num NUMBER(18) DEFAULT seq_t_history.NEXTVAL, --java long 대응
  user_num NUMBER(9) NOT NULL, --t_user user_num
  stock_code VARCHAR2(6) NOT NULL,
  trade_price NUMBER(9) NOT NULL, --t_stock_price price
  trade_cnt NUMBER(9) NOT NULL, --t_portfolio stock_cnt
  trade_date DATE DEFAULT SYSDATE NOT NULL,
  CONSTRAINT pk_t_history PRIMARY KEY (trade_num),
  CONSTRAINT ck_t_history_trade_price CHECK (trade_price>0),
  CONSTRAINT ck_t_history_trade_cnt CHECK (trade_cnt<>0)
);


CREATE SEQUENCE seq_t_comment START WITH 1 INCREMENT BY 1;

CREATE TABLE t_comment (
  comment_num NUMBER(18) DEFAULT seq_t_comment.NEXTVAL, --java long 대응
  post_num NUMBER(18) NOT NULL, --t_post post_num
  user_num NUMBER(9) NOT NULL, --t_user user_num
  comment_content VARCHAR2(300) NOT NULL, --30자 너무 짧을수도?->100자로
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