package com.example.payments.payment.service;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.dto.PaymentRequestDTO;
import com.example.payments.payment.repository.PaymentsTransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

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
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO();
        paymentRequestDTO.setCardNumber("1234567890123456");
        paymentRequestDTO.setExpiredNumber("2812");
        paymentRequestDTO.setInstallmentsMonths(0);
        paymentRequestDTO.setTotalAmount(1004L);
        paymentRequestDTO.setBusinessNumber("1231212345");
        paymentRequestDTO.setTerminalId("1001001234");

        //PaymentsTransaction paymentsTransaction = PaymentsTransaction.createTransaction(cardNumber, expireNumber, totalAmount, installmentsMonths, businessNumber, terminalId);
        
        //when: 테스트할 기능을 호출하고 실행하는 부분.
        Long transactionId = paymentsService.approveRequest(paymentRequestDTO);

        //then: 기대한 결과를 검증하는 부분. 특정한 결과나 동작이 예상대로 수행되었는지 확인한다.
        PaymentsTransaction getPaymentsTransaction = paymentsTransactionRepository.findOne(transactionId);
        assertEquals("정상 승이 났으면 승인상태는 APROVED", TransactionStatus.APPROVED, getPaymentsTransaction.getTransactionStatus());
    }

    @Test
    public void 카드번호_검증_오류() throws Exception {
        //given
        String cardNumber1 = "123456789012345";
        String cardNumber2 = "";
        String cardNumber3 = "12345678901234aa";
        String expireNumber = "2812";
        int installmentsMonths = 0;
        Long totalAmount = 1004L;
        String businessNumber = "1234567890";
        String terminalId = "1234567890";

        PaymentRequestDTO paymentRequestDTO1 = new PaymentRequestDTO();
        paymentRequestDTO1.setCardNumber(cardNumber1);
        paymentRequestDTO1.setExpiredNumber(expireNumber);
        paymentRequestDTO1.setInstallmentsMonths(installmentsMonths);
        paymentRequestDTO1.setTotalAmount(totalAmount);
        paymentRequestDTO1.setBusinessNumber(businessNumber);
        paymentRequestDTO1.setTerminalId(terminalId);

        PaymentRequestDTO paymentRequestDTO2 = new PaymentRequestDTO();
        paymentRequestDTO2.setCardNumber(cardNumber2);
        paymentRequestDTO2.setExpiredNumber(expireNumber);
        paymentRequestDTO2.setInstallmentsMonths(installmentsMonths);
        paymentRequestDTO2.setTotalAmount(totalAmount);
        paymentRequestDTO2.setBusinessNumber(businessNumber);
        paymentRequestDTO2.setTerminalId(terminalId);

        PaymentRequestDTO paymentRequestDTO3 = new PaymentRequestDTO();
        paymentRequestDTO2.setCardNumber(cardNumber2);
        paymentRequestDTO2.setExpiredNumber(expireNumber);
        paymentRequestDTO2.setInstallmentsMonths(installmentsMonths);
        paymentRequestDTO2.setTotalAmount(totalAmount);
        paymentRequestDTO2.setBusinessNumber(businessNumber);
        paymentRequestDTO2.setTerminalId(terminalId);

        //when
        Long transactionId1 = paymentsService.approveRequest(paymentRequestDTO1);

        Long transactionId2 = paymentsService.approveRequest(paymentRequestDTO2);

        Long transactionId3 = paymentsService.approveRequest(paymentRequestDTO3);

        //then
        PaymentsTransaction getPaymentsTransaction1 = paymentsTransactionRepository.findOne(transactionId1);
        PaymentsTransaction getPaymentsTransaction2 = paymentsTransactionRepository.findOne(transactionId2);
        PaymentsTransaction getPaymentsTransaction3 = paymentsTransactionRepository.findOne(transactionId3);

        assertThat(getPaymentsTransaction1.getTransactionStatus()).isEqualTo(TransactionStatus.REJECTED);
        assertThat(getPaymentsTransaction2.getTransactionStatus()).isEqualTo(TransactionStatus.REJECTED);
        assertThat(getPaymentsTransaction3.getTransactionStatus()).isEqualTo(TransactionStatus.REJECTED);

    }
}