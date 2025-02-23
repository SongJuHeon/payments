package com.example.payments.payment.repository;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.dto.CancelRequestDTO;

public interface PaymentsTransactionRepositoryCustom {
    void updateTransactionStatus(String id, TransactionStatus status);
    PaymentsTransaction findTransaction(CancelRequestDTO cancelRequestDTO);
}
