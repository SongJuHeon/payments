package com.example.payments.payment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "CREDIT_TRANSACTION")
@Getter
@Setter // 추후 세터 제거 예정
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

    // 도메인 핵심 로직 작성
}
