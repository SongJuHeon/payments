package com.example.payments.payment.repository;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.QPaymentsTransaction;
import com.example.payments.payment.domain.TransactionStatus;
import com.example.payments.payment.dto.CancelRequestDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentsTransactionRepositoryCustomImpl implements PaymentsTransactionRepositoryCustom {

    @PersistenceContext
    private EntityManager em; // EntityManager 주입

    private final JPAQueryFactory queryFactory;

    public PaymentsTransactionRepositoryCustomImpl(EntityManager em) {
        this.em = em; // EntityManager 초기화
        this.queryFactory = new JPAQueryFactory(em); // JPAQueryFactory 초기화
    }

    @Override
    @Transactional
    public void updateTransactionStatus(String id, TransactionStatus status) {
        em.createQuery("UPDATE PaymentsTransaction p SET p.transactionStatus = :status WHERE p.transactionId = :id")
                .setParameter("status", status)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public PaymentsTransaction findTransaction(CancelRequestDTO cancelRequestDTO) {
        QPaymentsTransaction paymentsTransaction = QPaymentsTransaction.paymentsTransaction;

        BooleanExpression booleanExpression = paymentsTransaction.approvalDate.eq(cancelRequestDTO.getOriginalApprovalDate())
                .and(paymentsTransaction.cardNumber.eq(cancelRequestDTO.getCardNumber()))
                .and(paymentsTransaction.approveNumber.eq(cancelRequestDTO.getOriginalApprovalNumber()))
                .and(paymentsTransaction.businessNumber.eq(cancelRequestDTO.getBusinessNumber()))
                .and(paymentsTransaction.totalAmount.eq(cancelRequestDTO.getTotalAmount()))
                .and(paymentsTransaction.installmentsMonths.eq(cancelRequestDTO.getInstallmentsMonths()))
                .and(paymentsTransaction.messageType.eq("0200"));

        return queryFactory.selectFrom(paymentsTransaction).where(booleanExpression).fetchOne();
    }
}
