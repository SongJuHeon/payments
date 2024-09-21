package com.example.payments.payment.service;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.repository.PaymentsTransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// PG 서비스 개발
@Service
@RequiredArgsConstructor
public class PaymentsService {

    @Autowired
    private final PaymentsTransactionRepository paymentsTransactionRepository;

    @Autowired
    private EntityManager entityManager;

    // 요청정보 저장
    // 요청정보 검증 -> 추후 기능 업데이트

    /* 승인요청 - 인자값 확정 필요
        카드번호, 유효기간, 금액, 할부개월, 카드사, 단말기번호
    */
    @Transactional  ///아래의 로직들을 하나의 트랜젝션으로 묶어줌
    // 메서드가 호출되면, 스프링에서 트랜잭션이 시작되고, 이 메서드 내 모든 작업이 하나의 트랜잭션으로 묶인다.
    // 해당 함수가 끝날 때 DB에 반영
    public Long approveRequest(String cardNumber, String expiredNumber, Long totalAmount
            , int installmentsMonths, String businessNumber, String terminalId) {
        // 실제로는 track2 형식으로 받겠지만, 일시적으로 카드번호, 유효기간으로 데이터 받음, 추후 리팩토링
        // 1. 트랜잭션 초기화
        PaymentsTransaction transaction = PaymentsTransaction.createTransaction(cardNumber, expiredNumber, totalAmount
                , installmentsMonths, businessNumber, terminalId);
        // 2. 초기 요청정보 저장
        // PaymentsTransaction 객체가 영속성 컨택스트로 들어감. 실제 DB에 insert 쿼리가 전송되지는 않음
        paymentsTransactionRepository.save(transaction);
        // 데이터 안정성을 위해 flush 이용하여 트랜잭션 커밋 진행. DB에 insert 되는 단계
        entityManager.flush();

        // 2. 검증 로직. 아래 내용들은 도메인에서??
        //   2-1. 유효한 가맹점인지
        //   2-2. 유효한 카드 정보인지 - 유효기간, 카드 유효성
        try {
            if(validateCard(transaction.getCardNumber())) {
                transaction.setTransactionStatus(TransactionStatus.REJECTED);
                transaction.setResponseMessage("Invalid card number");
                paymentsTransactionRepository.save(transaction); // 오류 상태와 메시지를 갱신
                return transaction.getTransactionId();
            }
            // 3. 승인상태로 변경
            // 더디 체킹에 의해 변경이 계속 감지되고 있음
            transaction.approve();

        } catch (Exception e) {
            transaction.setTransactionStatus(TransactionStatus.ERROR);
            transaction.setResponseMessage(e.getMessage());
            paymentsTransactionRepository.save(transaction);
        }

        // 4. 최종 정보 업데이트
        // 동일 트랜잭션 내에서 진행되므로 insert 쿼리가 실행되지 않음
        // 더티체킹 메커니즘이 활성화된 상태로 트랜잭션 커밋 시 변경이 감지되어 update 쿼리 수행
        paymentsTransactionRepository.save(transaction);

        return transaction.getTransactionId();
    }
    //함수가 종료되면 커밋이 되어 Insert 발생 - 중간에 flush를 해줬기 때문에 더티체킹에 의해 Update가 실행됨

    /// 취소요청
    public String cancelRequest(PaymentsTransaction paymentsTransaction) {

        return "1111";
    }

    private boolean validateCard(String cardNumber) {
        return cardNumber != null && cardNumber.length() != 16;
    }
}
