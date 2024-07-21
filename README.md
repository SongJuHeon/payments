# Project
Online payment Service

# 개요
온라인에서 사용자가 결제정보를 입력하여 상품 결제를 진행하는 서비스입니다.
결제, 결제 취소, 결제조회 세 가지 기능을 지원하며, 가맹점 정보 등은 등록이 되어 있다고 가정합니다(추후 해당 기능 개발 예정)
간단한 거래 정보 검증 후 통과되면 정상 승인 응답을 줍니다.(카드번호 유효성, 유효기간 체크, 금액 체크)
추후 버전 업데이트가 되면 기능을 추가할 예정입니다.

## 목차
- [설치](#설치)
- [사용법](#사용법)
- [기여](#기여)
- [라이선스](#라이선스)
- [업데이트 이력](#업데이트-이력)
- [요구사항 정의](#요구사항-정의)
- [설계](#설계)


## 업데이트 이력
### 2024.06.29 
- README 파일 수정

## 요구사항 정의
1. 결제
  - 결제시 카드번호, 유효기간, 생년월일, CVC, 비밀번호, 할부기간, 금액 정보를 입력한다. 데이터가 하나라도 비는 경우 진행되지 않는다.</br>
  - 카드번호는 하이픈 없이 숫자만 입력한다.</br>
  - 유효기간은 YYMM 형식으로 입력한다.</br>
  - 생년월일은 YYMMDD로 입력한다.</br>
  - 금액과 할부기간은 양수만 입력해야 하며, 할부기간은 일시불, 2개월, ..., n개월로 입력한다.</br>

  결제 결과는 성공, 실패가 있다.</br>
  - 성공시 주문번호, 카드번호, 금액, 가맹점명, 승인번호, 거래일자를 출력해야 한다.</br>
  - 실패시 주문번호, 결제실패사유를 출력해야 한다.</br>

  결제 응답을 받지 못하는 경우 망취소를 진행한다.

## 설계
결제.drawio 파일 참고
