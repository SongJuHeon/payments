package com.example.payments.payment.dto;

public class CancelRequestDTO {
    /// 원거래정보
    String originalApprovalDate;
    String originalApprovalNumber;

    String cardNumber;
    String expiredNumber;
    Long totalAmount;
    int installmentsMonths;
    String businessNumber;
    String terminalId;
    String transactionDate;
}
