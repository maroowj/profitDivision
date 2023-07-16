# 수익분배

* 🔊 프로젝트 소개
  * 회원들로 부터 투자금을 받아, 주식에 투자하여 얻은 수익을 투자금 비율에 맞게 분배하고 그 내역을 기록하는 웹서비스 프로젝트입니다.
  * 디자인과 퍼블리싱을 제외한 기존 DB, 백엔드, 프론트엔드까지 모두 담당했습니다.
  * 백엔드는 Restful-API를 개발하고, 프론트엔드는 Ajax(jQuery)로 API를 호출하는 방식으로 개발되었습니다.

* 🏗️개발 기간 및 인원 
  * 2022.11 ~ 2023.01
  * 풀스택 1명, 프론트엔드 1명 총 2명 중 풀스택 담당
  
* 🛠️사용 기술
  * Back-End: Spring Boot, Java, JPA, MySql, QueryDsl
  * Front-End: JavaScript, jQuery

* 💡부가기능
  * Spring-Scheduler를 이용하여 매달 회비 내역 DB 생성
  * Spring-Security 이용하여 관리자의 등급별 접근 페이지 제한 로직 구축
  * JWT 로그인 기능
  * JPA Batch insert를 사용하여 insert 시간 단축
    * 대용량 처리시 쿼리가 하나씩 나가는 것이 아니라, 1000건당 1번에 쿼리가 나가는 방식으로 변경

* 📅 DB
  * 테이블 정의서 : [https://docs.google.com/spreadsheets/d/1EfvZt1cfAv256R47ds3g9veVCGauua2B/edit?usp=sharing&ouid=113326887780897626343&rtpof=true&sd=true](https://docs.google.com/spreadsheets/d/1xnqnjgSr2JKbwS1AmQmF6r2vTa_NKYh6jS9U1yJjWpI/edit?usp=sharing)
  * ERD
![profit_division_erd](https://github.com/maroowj/profitDivision/assets/77284101/810b16c2-2c3d-4365-8c21-1cd49b572678)


* ✏️ 시나리오
  * 관리자
    * 슈퍼관리자외 부관리자를 생성하고 접근 가능한 페이지를 설정 할 수 있습니다.
    * 가입 신청한 회원들의 승인 여부를 관리합니다.
    * 회원을 등급별로 관리하고 등급별로 배당률을 관리합니다.
    * 회원들이 입금한 투자금 내역을 확인하고 저장합니다.
    * 매달 발생하는 회비 내역을 관리합니다.
    * 투자금으로 수익이 발생하면 수익금을 입력합니다. 입력 후 저장하면 즉시 수익금이 회원들에게 배당됩니다.
    * 출금을 신청한 회원들에게 입금해주고, 그 내역을 기록합니다.   
        
  * 사용자
    * 회원 가입 후 관리자에 의해 승인이 되면 투자 할 수 있습니다.
    * 관리자 계좌에 직접 입금 후 투자하기 메뉴를 통해 투자 내용을 입력합니다.
    * 투자가 확인 되면 투자 및 수익금이 포인트로 나타납니다.
    * 수익금은 언제든 출금 가능하며 출금 신청 메뉴에서 신청합니다.   
    
   
* 💻구동 화면
  * 사용자
![user_profit](https://github.com/maroowj/profitDivision/assets/77284101/fc93e200-2e27-476a-83fa-035bae5654ea)
![user_invest](https://github.com/maroowj/profitDivision/assets/77284101/3046c3f7-aedc-4bc0-accb-8b0b0c07f523)


  * 관리자
![admin_admin_manage](https://github.com/maroowj/profitDivision/assets/77284101/0a629056-86b5-47f4-8c8f-82c6a5c83911)
![admin_dues](https://github.com/maroowj/profitDivision/assets/77284101/abc808a6-e463-44f7-a18e-c6b2432b7f25)
![admin_profit](https://github.com/maroowj/profitDivision/assets/77284101/9278de4e-9c7f-4157-890e-026a4743fb70)



