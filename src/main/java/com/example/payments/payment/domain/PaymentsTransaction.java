package com.example.payments.payment.domain;

import com.example.payments.payment.dto.CancelRequestDTO;
import com.example.payments.payment.dto.PaymentRequestDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "CREDIT_TRANSACTION")
@Getter
@Setter // 추후 세터 제거 예정
public class PaymentsTransaction {
    /// 승인, 취소 요청의 모든 트랜잭션은 동일하게 초기화 되어야 한다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_Id")
    private Long transactionId;
    private String messageType = " ";
    private String terminalId = " ";
    private String merchantId = " ";
    private String businessNumber = " ";
    private String cardNumber = " ";
    private String expireNumber = " ";
    private Long totalAmount = 0L;
    private Long supplyAmount = 0L;
    private Long vatAmount = 0L;
    private Long serviceAmount = 0L;
    private Long taxAmount = 0L;
    private int installmentsMonths = 0;
    private String transactionDate = " ";
    private String transactionTime = " ";

    @Enumerated(EnumType.STRING)
    private IssuerCode issuerCode = IssuerCode.NONE;

    @Enumerated(EnumType.STRING)
    private PaymentsStatus paymentsStatus = PaymentsStatus.NONE;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus = TransactionStatus.NONE;

    private String approveNumber = " ";
    private String orderNumber = " ";

    private String responseCode = "9999";
    private String responseMessage = " ";
    private String approvalTime1 = " ";
    private String approvalTime2 = " ";

    /// 원거래정보
    private String originalApprovalDate = " ";
    private String originalApprovalTime = " ";
    private String originalApprovalNumber = " ";
    private String originalTransactionId = " ";

    // 생성자 메소드
    public PaymentsTransaction() {}
    public PaymentsTransaction (PaymentRequestDTO paymentRequestDTO) {
        //PaymentsTransaction transaction = new PaymentsTransaction();
        this.generateTransactionId();
        this.messageType = "0200";
        this.setCardNumber(paymentRequestDTO.getCardNumber());
        this.setExpireNumber(paymentRequestDTO.getExpiredNumber());
        this.setInstallmentsMonths(paymentRequestDTO.getInstallmentsMonths());
        this.setBusinessNumber(paymentRequestDTO.getBusinessNumber());
        this.setTerminalId(paymentRequestDTO.getTerminalId());
        this.setTotalAmount(paymentRequestDTO.getTotalAmount());
        this.setTransactionStatus(TransactionStatus.INITIATED);
        this.setTransactionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
        this.setTransactionTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
    }

    public PaymentsTransaction (CancelRequestDTO cancelRequestDTO) {
        this.generateTransactionId();
        this.messageType = "0210";
        this.setCardNumber(cancelRequestDTO.getCardNumber());
        this.setExpireNumber(cancelRequestDTO.getExpiredNumber());
        this.setInstallmentsMonths(cancelRequestDTO.getInstallmentsMonths());
        this.setBusinessNumber(cancelRequestDTO.getBusinessNumber());
        this.setTerminalId(cancelRequestDTO.getTerminalId());
        this.setTotalAmount(cancelRequestDTO.getTotalAmount());
        this.setTransactionStatus(TransactionStatus.INITIATED);

        /// 원거래 정보
        this.setOriginalApprovalDate(cancelRequestDTO.getOriginalApprovalDate());
        this.setOriginalApprovalNumber(cancelRequestDTO.getOriginalApprovalNumber());

        this.setTransactionDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
        this.setTransactionTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss")));
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    // 도메인 핵심 로직 작성
    public void approve() {
        if (this.transactionStatus != TransactionStatus.INITIATED){
            throw new IllegalStateException("Cannot approve unless initiated");
        }
        this.transactionStatus = TransactionStatus.APPROVED;
        this.setApprovalTime1(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        // 승인번호 생성
        if (this.messageType == "0200") {
            this.setApproveNumber(createApproveNumber());
        }
        else {
            this.setApproveNumber(this.getOriginalApprovalNumber());
        }
        this.setResponseCode("0000");
        this.setResponseMessage("정상 처리");
    }

    public void cancel() {

    }

    public String createApproveNumber() {
        Random random = new Random();

        int randomNumber = 10000000 + random.nextInt(90000000);

        return Integer.toString(randomNumber);
    }
}
