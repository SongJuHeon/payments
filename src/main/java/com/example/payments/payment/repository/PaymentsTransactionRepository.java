package com.example.payments.payment.repository;

import com.example.payments.payment.domain.PaymentsTransaction;
import com.example.payments.payment.domain.QPaymentsTransaction;
import com.example.payments.payment.dto.CancelRequestDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    public List<PaymentsTransaction> findTransaction(String approvalDate, String cardNumber) {
        return em.createQuery("select p from PaymentsInfo p where p.approvalDate = :approvalDate" +
                "and p.cardNumber = :cardNumber")
                .setParameter("approvalDate", approvalDate)
                .setParameter("cardNumber", cardNumber)
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

        return (PaymentsTransaction) queryFactory.selectFrom(paymentsTransaction).where(booleanExpression).fetch();
    }
}
