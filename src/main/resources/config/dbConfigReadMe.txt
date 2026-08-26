현재있는 src/main/resources/config 폴더 안에 db.properties 라는 파일을 만들어 줍니다.

db.driver=oracle.jdbc.OracleDriver
db.username=scott
db.password=tiger

# 연결할 db 주소를 넣어줍니다
db.url=jdbc:oracle:thin:@localhost:1521:orcl

#공용DB
#db.username=ratel
#db.password=association
#db.url=jdbc:oracle:thin:@//192.168.0.7:1521/orclpdb

를 넣고 저장.