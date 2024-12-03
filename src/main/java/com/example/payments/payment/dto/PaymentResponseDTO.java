package com.example.payments.payment.dto;

import com.example.payments.payment.domain.PaymentsTransaction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseDTO {
    Long transactionId;
    String cardNumber;
    String expiredNumber;
    Long totalAmount;
    int installmentsMonths;
    String businessNumber;
    String terminalId;
    String approveNumber;
    String responseMessage;
    String approvalDate;
    String approvalTime;

    public static PaymentResponseDTO createPaymentResponseDTO(PaymentsTransaction transaction) {
        PaymentResponseDTO paymentResponseDTO = new PaymentResponseDTO();

        paymentResponseDTO.setTransactionId(transaction.getTransactionId());
        paymentResponseDTO.setCardNumber(transaction.getCardNumber());
        paymentResponseDTO.setExpiredNumber(transaction.getExpireNumber());
        paymentResponseDTO.setTotalAmount(transaction.getTotalAmount());
        paymentResponseDTO.setInstallmentsMonths(transaction.getInstallmentsMonths());
        paymentResponseDTO.setBusinessNumber(transaction.getBusinessNumber());
        paymentResponseDTO.setTerminalId(transaction.getTerminalId());
        paymentResponseDTO.setApproveNumber(transaction.getApproveNumber());
        paymentResponseDTO.setResponseMessage(transaction.getResponseMessage());
        paymentResponseDTO.setApprovalDate(transaction.getTransactionDate());
        paymentResponseDTO.setApprovalTime(transaction.getTransactionTime());

        return paymentResponseDTO;
    }
}
