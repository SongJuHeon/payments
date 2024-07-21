package com.example.payments.paymentInfo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter // 추후 세터 제거 예정
public class PaymentsInfo {
    @Id @GeneratedValue
    @Column(name = "transaction_Id")
    private String transactionId;

    private String merchantId;
    private Long totalAmount;
    private Long supplyAmount;
    private Long vatAmount;
    private Long serviceAmount;
    private Long taxAmount;
    private String approveNumber;
    private String orderNumber;
    private String approvalDate;
    private String approvalTime;
    private String responseCode;
    private String responseMessage;
    private String approvalTime1;
    private String approvalTime2;
}
