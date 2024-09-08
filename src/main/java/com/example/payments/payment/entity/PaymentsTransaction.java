package com.example.payments.payment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Entity
@Table(name = "CREDIT_TRANSACTION")
@Getter
@Setter // 추후 세터 제거 예정
@NoArgsConstructor(access = AccessLevel.PROTECTED) //임시로 설정. 추후 생성자 인자 결정되면 제거
public class PaymentsTransaction {
    @Id @GeneratedValue
    @Column(name = "transaction_Id")
    private String transactionId;

    private String merchantId;
    private String businessNumber;
    private Long totalAmount;
    private Long supplyAmount;
    private Long vatAmount;
    private Long serviceAmount;
    private Long taxAmount;
    private String transactionDate;
    private String transactionTime;

    @Enumerated(EnumType.STRING)
    private IssuerCode issuerCode;

    @Enumerated(EnumType.STRING)
    private PaymentsStatus paymentsStatus;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    private String approveNumber;
    private String orderNumber;

    private String approvalDate;
    private String approvalTime;
    private String responseCode;
    private String responseMessage;
    private String approvalTime1;
    private String approvalTime2;

    // 생성자 메소드
    public static PaymentsTransaction createTransaction (Object object) {
        PaymentsTransaction transaction = new PaymentsTransaction();
        transaction.setTotalAmount(1004L);
        transaction.setTransactionStatus(TransactionStatus.INITIATED);
        transaction.setTransactionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
        transaction.setTransactionTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
        transaction.setApprovalTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));

        return transaction;
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
    }

    public void cancel() {

    }

    public String createApproveNumber() {
        Random random = new Random();

        int randomNumber = 10000000 + random.nextInt(90000000);

        return Integer.toString(randomNumber);
    }
}
