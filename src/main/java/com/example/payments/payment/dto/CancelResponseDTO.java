package com.example.payments.payment.dto;

import com.example.payments.payment.domain.PaymentsTransaction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CancelResponseDTO {
    String transactionId;
    String transactionDate = " ";
    String transactionTime = " ";
    String cardNumber = " ";
    String expiredNumber = " ";
    Long totalAmount = 0L;
    int installmentsMonths = 0;
    String businessNumber = " ";
    String terminalId = " ";
    String approveDate = " ";
    String approveNumber = " ";
    String responseCode = " ";
    String responseMessage = " ";
    Long originalTransactionId = 0L;
}
