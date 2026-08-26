# frade
## 2차 1팀 프로젝트
### 모의거래 + 커뮤니티 서비스

**필독**
프로젝트 clone 한 뒤 반드시 src/main/resources에 있는 dbConfigReadMe.txt 파일을 읽고 db.properties 파일을 만들어주세요.

**프로젝트 규칙**
1. DTO클래스 만들 때 dto패키지 내에 클래스 이름이 겹치지 않도록 만들어주세요.
   mybatis-config에 따로 alias(별칭) 등록하지 않아도 자동으로 등록됩니다.
2. Pull request사용
   vscode 확장프로그램 github pull requests 검색 후 설치
   평소대로 개인 브랜치에서 커밋 및 push 해주시고 merge를 할 타이밍이 오면
   
   <img width="300" height="170" alt="image" src="https://github.com/user-attachments/assets/00c7a0b2-7f13-4b7a-a7a7-27e217e68d4c" />
   <img width="43" height="141" alt="image" src="https://github.com/user-attachments/assets/754e8e0e-ffdf-4508-b5d1-cfd2dace8091" />
   
   이 create pull request 버튼을 눌러줍니다
   
   <img width="295" height="417" alt="image" src="https://github.com/user-attachments/assets/cbe6ac20-2ef4-4110-8663-0b358346ac77" />
   
   어느 브랜치에서 어느 브랜치로 merge 할건지 선택해서 create를 눌러줍니다(설명을 적어주면 더 좋습니다)
   열린 pull request는 다른사람이 보고 코멘트를 남겨도 좋고, 별다른 문제 없으면 기술팀장이 merge 한 뒤 알림을 보내겠습니다.

3. 깃&깃허브 협업 규칙 <br>
   3-1 브랜치 전략 <br>
      main(최종배포용) -> dev(테스트 통합용) -> feature/도메인(개인작업용) 형태의 구조로 진행하며<br>
      ✔ 기능 개발을 feature 브랜치에서 하는게 아님!! <br>
      ✔✔ 로그인 기능 개발  : feature/login <br>
      ✔✔ 로그아웃 기능 개발: feature/logout <br>
      ✔✔✔feature 하위에 브랜치를 따서 기능별로 관리 <br>
   3-2 커밋 메시지 약속 <br>
      [FEAT] 회원가입 기능 추가, [FIX] 로그인 오류 수정, [CHORE] 오타 수정 등<br>
      말머리를 달아 남들이 작업 내역을 한눈에 파악할 수 있게 합니다.
   
4. 깃 협업 흐름(그림)
   <img width="1198" height="749" alt="브랜치 전략" src="https://github.com/user-attachments/assets/b0644c19-5845-4f3d-8b05-eef54432625c" />

5. JSON 응답(Response) 규격 <br>
   API 요청이 성공하든, 실패하든, 서버가 다운되든 프론트엔드로 내려가는 JSON 데이터의 '껍데기(Wrapper)'는 무조건 똑같이 생겨야 합니다.<br>

   <pre> <code>    
       {<br>
            "code": 200,             // HTTP 상태 코드 또는 자체 비즈니스 코드<br>
            "message": "성공",         // 프론트엔드에서 띄워줄 알림 메시지<br>
            "data": { ... }          // 실제 화면에 그릴 데이터 (실패 시 null)<br>
        }<br>
   </code> </pre>

6. 리소스접근 할 때(css,image,js) 경로명 앞에 "/resources" 넣어서 경로 적어주기!

7. 주석!!!!!!!

