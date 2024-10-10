package com.example.payments.payment.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentsRequest {
    private String transactionDate;
    private String cardNumber;
    private String expirationDate;
    private Long amount;
    private Integer installmentMonths;
}
