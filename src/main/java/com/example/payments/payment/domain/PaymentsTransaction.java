package com.example.payments.payment.domain;

import com.example.payments.payment.dto.PaymentRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "CREDIT_TRANSACTION")
@Getter
@Setter // 추후 세터 제거 예정
@NoArgsConstructor(access = AccessLevel.PROTECTED) //임시로 설정. 추후 생성자 인자 결정되면 제거
public class PaymentsTransaction {
    /// 승인, 취소 요청의 모든 트랜잭션은 동일하게 초기화 되어야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_Id")
    private Long transactionId;

    private String terminalId = " ";
    private String merchantId = " ";
    private String businessNumber = " ";
    private String cardNumber = " ";
    private String expireNumber = " ";
    private Long totalAmount = 0L;
    private Long supplyAmount = 0L;
    private Long vatAmount = 0L;
    private Long serviceAmount = 0L;
    private Long taxAmount = 0L;
    private int installmentsMonths = 0;
    private String transactionDate = " ";
    private String transactionTime = " ";

    @Enumerated(EnumType.STRING)
    private IssuerCode issuerCode = IssuerCode.NONE;

    @Enumerated(EnumType.STRING)
    private PaymentsStatus paymentsStatus = PaymentsStatus.NONE;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus = TransactionStatus.NONE;

    private String approveNumber = " ";
    private String orderNumber = " ";

    private String responseCode = "9999";
    private String responseMessage = " ";
    private String approvalTime1 = " ";
    private String approvalTime2 = " ";

    /// 원거래정보
    private String originalApprovalDate;
    private String originalApprovalTime;
    private String originalApprovalNumber;
    private String originalTransactionId;

    // 생성자 메소드
    public static PaymentsTransaction createTransaction (PaymentRequestDTO paymentRequestDTO) {
        PaymentsTransaction transaction = new PaymentsTransaction();
        transaction.generateTransactionId();
        transaction.setCardNumber(paymentRequestDTO.getCardNumber());
        transaction.setExpireNumber(paymentRequestDTO.getExpiredNumber());
        transaction.setInstallmentsMonths(paymentRequestDTO.getInstallmentsMonths());
        transaction.setBusinessNumber(paymentRequestDTO.getBusinessNumber());
        transaction.setTerminalId(paymentRequestDTO.getTerminalId());
        transaction.setTotalAmount(paymentRequestDTO.getTotalAmount());
        transaction.setTransactionStatus(TransactionStatus.INITIATED);
        transaction.setTransactionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
        transaction.setTransactionTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));

        return transaction;
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    // 도메인 핵심 로직 작성
    public void approve() {
        if (this.transactionStatus != TransactionStatus.INITIATED){
            throw new IllegalStateException("Cannot approve unless initiated");
        }
        this.transactionStatus = TransactionStatus.APPROVED;
        this.setApprovalTime1(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        // 승인번호 생성
        this.setApproveNumber(createApproveNumber());
        this.setResponseCode("0000");
        this.setResponseMessage("정상 승인");
    }

    public void cancel() {

    }

    public String createApproveNumber() {
        Random random = new Random();

        int randomNumber = 10000000 + random.nextInt(90000000);

        return Integer.toString(randomNumber);
    }
}
