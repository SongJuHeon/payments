package com.example.payments.payment.service;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.repository.PaymentsTransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PaymentsServiceTest {

    @Autowired
    PaymentsService paymentsService;

    @Autowired
    PaymentsTransactionRepository paymentsTransactionRepository;

    @Autowired
    EntityManager em;
    
    @Test
    public void 정상승인() throws Exception {
        //given: 주어진 상황을 설정하는 부분.
        String cardNumber = "1234567890123456";
        String expireNumber = "2812";
        int installmentsMonths = 0;
        Long totalAmount = 1004L;
        String businessNumber = "1234567890";
        String terminalId = "1234567890";

        //PaymentsTransaction paymentsTransaction = PaymentsTransaction.createTransaction(cardNumber, expireNumber, totalAmount, installmentsMonths, businessNumber, terminalId);
        
        //when: 테스트할 기능을 호출하고 실행하는 부분.
        Long transactionId = paymentsService.approveRequest(cardNumber, expireNumber, totalAmount, installmentsMonths
                , businessNumber, terminalId);

        //then: 기대한 결과를 검증하는 부분. 특정한 결과나 동작이 예상대로 수행되었는지 확인한다.
        PaymentsTransaction getPaymentsTransaction = paymentsTransactionRepository.findOne(transactionId);
        System.out.println(getPaymentsTransaction.getCardNumber().length());
        assertEquals("정상 승이 났으면 승인상태는 APROVED", TransactionStatus.APPROVED, getPaymentsTransaction.getTransactionStatus());

    }
}