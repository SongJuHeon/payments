package com.example.payments.payment.repository;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.QPaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.dto.CancelRequestDTO;
import com.example.payments.payment.dto.PaymentResponseDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentsTransactionRepository {

    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory;

    /// 단순 CRUD는 entitymanager로, 조회같은 복잡한 쿼리는 queryFactory 이용
    public PaymentsTransactionRepository(EntityManager em) {
        this.em = em; // EntityManager 초기화
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 초기화
    }

    public void save(PaymentsTransaction paymentsTransaction) {
        em.persist(paymentsTransaction);
    }

    public PaymentsTransaction findOne(Long id) {
        QPaymentsTransaction paymentsTransaction = QPaymentsTransaction.paymentsTransaction;
        return queryFactory.selectFrom(paymentsTransaction)
                .where(paymentsTransaction.transactionId.eq(id))
                .fetchOne(); // 조건에 맞는 단일 결과 반환
    }

    // 거래 상태 업데이트 메서드
    @Transactional
    public void updateTransactionStatus(Long id, TransactionStatus status) {
        // JPQL 쿼리를 사용하여 거래 상태 업데이트
        em.createQuery("UPDATE PaymentsTransaction p SET p.transactionStatus = :status WHERE p.id = :id")
                .setParameter("status", status)
                .setParameter("id", id)
                .executeUpdate();
    }

    public List<PaymentsTransaction> findTransaction(PaymentResponseDTO paymentResponseDTO) {
        return em.createQuery("select p from paymentsTransaction p where p.approvalDate = :approvalDate" +
                "and p.cardNumber = :cardNumber"
                + "and p.approveNumber = :approveNumber"
                + "and p.businessNumber = :businessNumber"
                + "and p.totalAmount = :totalAmount"
                + "and p.installmentsMonths = :installmentsMonths")
                .setParameter("approvalDate", paymentResponseDTO.getApprovalDate())
                .setParameter("cardNumber", paymentResponseDTO.getCardNumber())
                .setParameter("approveNumber", paymentResponseDTO.getApproveNumber())
                .setParameter("businessNumber", paymentResponseDTO.getBusinessNumber())
                .setParameter("totalAmount", paymentResponseDTO.getTotalAmount())
                .setParameter("installmentsMonths", paymentResponseDTO.getInstallmentsMonths())
                .getResultList();
    }

    public PaymentsTransaction findTransaction(CancelRequestDTO cancelRequestDTO) {
        QPaymentsTransaction paymentsTransaction = QPaymentsTransaction.paymentsTransaction;
        BooleanExpression booleanExpression = paymentsTransaction.transactionDate.eq(cancelRequestDTO.getOriginalApprovalDate())
                .and(paymentsTransaction.cardNumber.eq(cancelRequestDTO.getCardNumber()))
                .and(paymentsTransaction.approveNumber.eq(cancelRequestDTO.getOriginalApprovalNumber()))
                .and(paymentsTransaction.businessNumber.eq(cancelRequestDTO.getBusinessNumber()))
                .and(paymentsTransaction.totalAmount.eq(cancelRequestDTO.getTotalAmount()))
                .and(paymentsTransaction.installmentsMonths.eq(cancelRequestDTO.getInstallmentsMonths()));

        return queryFactory.selectFrom(paymentsTransaction).where(booleanExpression).fetchOne();
    }
}
