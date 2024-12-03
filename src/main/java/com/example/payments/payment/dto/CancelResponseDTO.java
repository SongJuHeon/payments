package com.example.payments.payment.dto;

import com.example.payments.payment.domain.PaymentsTransaction;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelResponseDTO {
    String cardNumber;
    String expiredNumber;
    Long totalAmount;
    int installmentsMonths;
    String businessNumber;
    String terminalId;
    String approveNumber;
    String responseMessage;
    Long originalTransactionId;

    public static CancelResponseDTO createCancelResponseDTO(PaymentsTransaction transaction) {
        CancelResponseDTO cancelResponseDTO = new CancelResponseDTO();

        cancelResponseDTO.setCardNumber(transaction.getCardNumber());
        cancelResponseDTO.setExpiredNumber(transaction.getExpireNumber());
        cancelResponseDTO.setTotalAmount(transaction.getTotalAmount());
        cancelResponseDTO.setInstallmentsMonths(transaction.getInstallmentsMonths());
        cancelResponseDTO.setBusinessNumber(transaction.getBusinessNumber());
        cancelResponseDTO.setTerminalId(transaction.getTerminalId());
        cancelResponseDTO.setApproveNumber(transaction.getApproveNumber());
        cancelResponseDTO.setResponseMessage(transaction.getResponseMessage());
        cancelResponseDTO.setOriginalTransactionId(transaction.getTransactionId());

        return cancelResponseDTO;
    }
}
