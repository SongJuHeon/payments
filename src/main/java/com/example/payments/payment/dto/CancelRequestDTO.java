package com.example.payments.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
