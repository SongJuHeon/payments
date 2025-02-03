package com.example.payments.payment.service;

import com.example.payments.payment.data.CodeData.ResponseCode;
import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.dto.CancelRequestDTO;
import com.example.payments.payment.dto.CancelResponseDTO;
import com.example.payments.payment.dto.PaymentRequestDTO;
import com.example.payments.payment.dto.PaymentResponseDTO;
import com.example.payments.payment.exception.InvalidCardException;
import com.example.payments.payment.repository.PaymentsTransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
        String transactionId = generateTransactionId();
        String messageType = "0200";
        String transactionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String transactionTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

        PaymentsTransaction transaction = PaymentsTransaction.builder()
                .transactionId(transactionId)
                .messageType(messageType)
                .cardNumber(paymentRequestDTO.getCardNumber())
                .expireNumber(paymentRequestDTO.getExpiredNumber())
                .installmentsMonths(paymentRequestDTO.getInstallmentsMonths())
                .businessNumber(paymentRequestDTO.getBusinessNumber())
                .terminalId(paymentRequestDTO.getTerminalId())
                .totalAmount(paymentRequestDTO.getTotalAmount())
                .transactionStatus(TransactionStatus.INITIATED)
                .transactionDate(transactionDate)
                .transactionTime(transactionTime)
                .build();

        // 2. 초기 요청정보 저장
        // 데이터 안정성을 위해 flush 이용하여 트랜잭션 커밋 진행. DB에 insert 되는 단계
        try {
            paymentsTransactionRepository.save(transaction);
            paymentsTransactionRepository.flush();
        } catch (Exception e) {
            return createErrorResponse(
                    transactionId
                    , paymentRequestDTO
                    , ResponseCode.SYSTEM_ERROR.InternalSystemError.code
                    , ResponseCode.SYSTEM_ERROR.InternalSystemError.message);
        }

        // 2. 검증 로직. 아래 내용들은 도메인에서?? 예외처리로 빼는건 장애 발생등의 상황이고, 값 검증 오류는 정상적인 로직
        // 2. 카드 유효성 검증
        if (!validateCard(transaction.getCardNumber())) {
            transaction.setTransactionStatus(TransactionStatus.REJECTED);
            transaction.setResponseCode(ResponseCode.PAYMENT_ERROR.InvalidCardNumber.code);
            transaction.setResponseMessage(ResponseCode.PAYMENT_ERROR.InvalidCardNumber.message);
        }
        //   2-1. 유효한 가맹점인지
        //   2-2. 유효한 카드 정보인지 - 유효기간, 카드 유효성

        // 정상적이면 승인 처리 후 응답값 반환
        if (transaction.getTransactionStatus() == TransactionStatus.INITIATED) {
            transaction.approve();
        }

        /// 최종 상태 저장
        try {
            paymentsTransactionRepository.save(transaction);

            PaymentResponseDTO paymentResponseDTO = PaymentResponseDTO.builder()
                    .transactionId(transactionId)
                    .cardNumber(paymentRequestDTO.getCardNumber())
                    .expiredNumber(paymentRequestDTO.getExpiredNumber())
                    .totalAmount(paymentRequestDTO.getTotalAmount())
                    .installmentsMonths(paymentRequestDTO.getInstallmentsMonths())
                    .businessNumber(paymentRequestDTO.getBusinessNumber())
                    .terminalId(paymentRequestDTO.getTerminalId())
                    .approvalDate(transaction.getTransactionDate())
                    .approvalTime(transaction.getApprovalTime1())
                    .approveNumber(transaction.getApproveNumber())
                    .responseCode(transaction.getResponseCode())
                    .responseMessage(transaction.getResponseMessage())
                    .build();

            return paymentResponseDTO;

        } catch (Exception e) {
            return createErrorResponse(
                    transactionId
                    , paymentRequestDTO
                    , ResponseCode.SYSTEM_ERROR.InternalSystemError.code
                    , ResponseCode.SYSTEM_ERROR.InternalSystemError.message
            );
        }
    }
    //함수가 종료되면 커밋이 되어 Insert 발생 - 중간에 flush를 해줬기 때문에 더티체킹에 의해 Update가 실행됨

    /// 취소요청 - 20250203 기준 수정중
    @Transactional
    public CancelResponseDTO cancelRequest(CancelRequestDTO cancelRequestDTO) {

        /// 1. 초기 취소 요청정보 insert
        String transactionId = generateTransactionId();
        String messageType = "0210";
        String transactionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String transactionTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));

        PaymentsTransaction transaction = PaymentsTransaction.builder()
                .transactionId(transactionId)
                .messageType(messageType)
                .cardNumber(cancelRequestDTO.getCardNumber())
                .expireNumber(cancelRequestDTO.getExpiredNumber())
                .installmentsMonths(cancelRequestDTO.getInstallmentsMonths())
                .businessNumber(cancelRequestDTO.getBusinessNumber())
                .terminalId(cancelRequestDTO.getTerminalId())
                .totalAmount(cancelRequestDTO.getTotalAmount())
                .originalApprovalDate(cancelRequestDTO.getOriginalApprovalDate())
                .originalApprovalNumber(cancelRequestDTO.getOriginalApprovalNumber())
                .transactionStatus(TransactionStatus.INITIATED)
                .transactionDate(transactionDate)
                .transactionTime(transactionTime)
                .build();

        CancelResponseDTO cancelResponseDTO = CancelResponseDTO.builder()
                .transactionId(transactionId)
                .cardNumber(cancelRequestDTO.getCardNumber())
                .expiredNumber(cancelRequestDTO.getExpiredNumber())
                .totalAmount(cancelRequestDTO.getTotalAmount())
                .installmentsMonths(cancelRequestDTO.getInstallmentsMonths())
                .businessNumber(cancelRequestDTO.getBusinessNumber())
                .terminalId(cancelRequestDTO.getTerminalId())
                .approveDate(cancelRequestDTO.getOriginalApprovalDate())
                .approveNumber(cancelRequestDTO.getOriginalApprovalNumber())
                .build();

        // 2. 초기 요청정보 저장
        // 데이터 안정성을 위해 flush 이용하여 트랜잭션 커밋 진행. DB에 insert 되는 단계
        try {
            paymentsTransactionRepository.save(transaction);
            paymentsTransactionRepository.flush();
        } catch(Exception e) {
            PaymentResponseDTO.builder()
                    .responseCode(ResponseCode.SYSTEM_ERROR.InternalSystemError.code)
                    .responseMessage(ResponseCode.SYSTEM_ERROR.InternalSystemError.message)
                    .build();

            return cancelResponseDTO;
        }

        /// 2. 거래 유효정보 검증 - 현재 단계는 카드번호만 검증
        if (!validateCard(transaction.getCardNumber())) {
            transaction = PaymentsTransaction.builder()
                    .transactionStatus(TransactionStatus.REJECTED)
                    .responseCode(ResponseCode.PAYMENT_ERROR.InvalidCardNumber.code)
                    .responseMessage(ResponseCode.PAYMENT_ERROR.InvalidCardNumber.message)
                    .build();

            // 예외 발생 시 오류 응답 DTO 생성
            cancelResponseDTO.builder()
                    .responseCode(transaction.getResponseCode())
                    .responseMessage(transaction.getResponseMessage())
                    .build();
        }
        /// 3. 원거래 검색 - 못찾으면 거절, 금액 정보가 다르면 거절, 이미 취소된 거래면 거절
        PaymentsTransaction originalPayments = paymentsTransactionRepository.findTransaction(cancelRequestDTO);

        if (originalPayments == null) {
            paymentsTransaction.setTransactionStatus(TransactionStatus.REJECTED);
            paymentsTransaction.setResponseMessage("Original transaction not found");

            return CancelResponseDTO.createCancelResponseDTO(paymentsTransaction);
        }

        if (originalPayments.getTotalAmount().compareTo(cancelRequestDTO.getTotalAmount()) != 0) {
            paymentsTransaction.setTransactionStatus(TransactionStatus.REJECTED);
            paymentsTransaction.setResponseMessage("Mismatch in transaction amount");

            return CancelResponseDTO.createCancelResponseDTO(paymentsTransaction);
        }

        if (originalPayments.getTransactionStatus() == TransactionStatus.CANCELLED) {
            paymentsTransaction.setTransactionStatus(TransactionStatus.REJECTED);
            paymentsTransaction.setResponseMessage("Transaction already canceled");

            return CancelResponseDTO.createCancelResponseDTO(paymentsTransaction);
        }
        /// querydsl로 일단 어찌어찌 해서 적용하고 후에 강의보고 공부하는걸로 하자

        // /// 5. 원거래 상태 변경 및 저장
        originalPayments.setTransactionStatus(TransactionStatus.CANCELLED);
        paymentsTransactionRepository.updateTransactionStatus(
                originalPayments.getTransactionId(), TransactionStatus.CANCELLED);

        /// 6. 현재 트랜잭션 업데이트
        /// 7. 응답 전달
        paymentsTransaction.approve();
        paymentsTransactionRepository.save(paymentsTransaction);

        return CancelResponseDTO.createCancelResponseDTO(paymentsTransaction);
    }

    /// 여러 검증 규칙이 있으나 간단하게 서버를 구축하는게 목적이므로 간소화 했음
    /// 2024.10.30 아래 로직 exception이 아니라 비즈니스 로직으로 변경할 예정
    private boolean validateCard(String cardNumber) {
        return cardNumber != null && cardNumber.length() == 16 && cardNumber.chars().allMatch(Character::isDigit);
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    // 중복 코드 제거를 위한 오류 응답 생성 메서드
    private PaymentResponseDTO createErrorResponse(String transactionId, PaymentRequestDTO paymentRequestDTO, String responseCode, String responseMessage) {
        return PaymentResponseDTO.builder()
                .transactionId(transactionId)
                .cardNumber(paymentRequestDTO.getCardNumber())
                .expiredNumber(paymentRequestDTO.getExpiredNumber())
                .totalAmount(paymentRequestDTO.getTotalAmount())
                .installmentsMonths(paymentRequestDTO.getInstallmentsMonths())
                .businessNumber(paymentRequestDTO.getBusinessNumber())
                .terminalId(paymentRequestDTO.getTerminalId())
                .responseCode(responseCode)
                .responseMessage(responseMessage)
                .build();
    }
}
