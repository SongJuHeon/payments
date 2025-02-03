package com.example.payments.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponseDTO {
    String transactionId;
    String cardNumber;
    String expiredNumber;
    Long totalAmount;
    int installmentsMonths;
    String businessNumber;
    String terminalId;
    String approveNumber;
    String responseCode;
    String responseMessage;
    String approvalDate;
    String approvalTime;
}
