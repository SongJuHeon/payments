package com.example.payments.payment.service;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.dto.PaymentRequestDTO;
import com.example.payments.payment.dto.PaymentResponseDTO;
import com.example.payments.payment.exception.InvalidCardException;
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
    public PaymentResponseDTO approveRequest(PaymentRequestDTO paymentRequestDTO) {
        // 실제로는 track2 형식으로 받겠지만, 일시적으로 카드번호, 유효기간으로 데이터 받음, 추후 리팩토링
        // 1. 트랜잭션 초기화
        PaymentsTransaction transaction = PaymentsTransaction.createTransaction(paymentRequestDTO);
        // 2. 초기 요청정보 저장
        // PaymentsTransaction 객체가 영속성 컨택스트로 들어감. 실제 DB에 insert 쿼리가 전송되지는 않음
        paymentsTransactionRepository.save(transaction);
        // 데이터 안정성을 위해 flush 이용하여 트랜잭션 커밋 진행. DB에 insert 되는 단계
        entityManager.flush();

        // 2. 검증 로직. 아래 내용들은 도메인에서?? 예외처리로 빼는건 장애 발생등의 상황이고, 값 검증 오류는 정상적인 로직
        //   2-1. 유효한 가맹점인지
        //   2-2. 유효한 카드 정보인지 - 유효기간, 카드 유효성
        try {
            validateCard(transaction.getCardNumber());
            // 3. 승인상태로 변경
            // 더디 체킹에 의해 변경이 계속 감지되고 있음
            transaction.approve();

        } catch(InvalidCardException e) {
            transaction.setTransactionStatus(TransactionStatus.REJECTED);
            transaction.setResponseMessage("Invalid card number");


        } catch (IllegalStateException e) {
            transaction.setTransactionStatus(TransactionStatus.ERROR);
            transaction.setResponseMessage(e.getMessage());
        } finally {
            paymentsTransactionRepository.save(transaction); // 오류 상태와 메시지를 갱신
        }

        paymentsTransactionRepository.save(transaction);

        return PaymentResponseDTO.createPaymentResponseDTO(transaction);
    }
    //함수가 종료되면 커밋이 되어 Insert 발생 - 중간에 flush를 해줬기 때문에 더티체킹에 의해 Update가 실행됨

    /// 취소요청
    public String cancelRequest(PaymentsTransaction paymentsTransaction) {

        /// 1. 초기 취소 요청정보 insert
        /// 2. 거래 유효정보 검증 - 현재 단계는 카드번호만 검증
        /// 3. 원거래 검색 - 못찾으면 거절, 금액 정보가 다르면 거절, 이미 취소된 거래면 거절
        /// 4. 원거래 정보를 현재 트랜잭션에 저장.
        /// 5. 원거래 상태 변경
        /// 6. 현재 트랜잭션 업데이트
        /// 7. 응답 전달

        return "1111";
    }

    /// 여러 검증 규칙이 있으나 간단하게 서버를 구축하는게 목적이므로 간소화 했음
    /// 2024.10.30 아래 로직 exception이 아니라 비즈니스 로직으로 변경할 예정
    private void validateCard(String cardNumber) {
        if (cardNumber == null || cardNumber.length() != 16 || !cardNumber.chars().allMatch(Character::isDigit)) {
            throw new InvalidCardException();
        }
    }
}
