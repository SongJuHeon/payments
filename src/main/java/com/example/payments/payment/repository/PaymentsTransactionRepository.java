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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentsTransactionRepository extends JpaRepository<PaymentsTransaction, Long>, JpaSpecificationExecutor<PaymentsTransaction> {

    // 기본적인 CRUD 메서드는 JpaRepository에서 자동으로 제공됩니다.

    // EntityManager 주입
    @PersistenceContext
    EntityManager em = null; // EntityManager를 주입받을 수는 없지만, 메서드에서 사용하도록 유지

    // JPQL을 이용한 거래 상태 업데이트 메서드
    @Transactional
    default void updateTransactionStatus(Long id, TransactionStatus status) {
        em.createQuery("UPDATE PaymentsTransaction p SET p.transactionStatus = :status WHERE p.id = :id")
                .setParameter("status", status)
                .setParameter("id", id)
                .executeUpdate();
    }

    // JPAQueryFactory를 이용한 복잡한 쿼리 메서드
    // QueryDSL을 사용하기 위해 별도의 메서드를 추가하였습니다.
    default PaymentsTransaction findTransaction(CancelRequestDTO cancelRequestDTO) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QPaymentsTransaction paymentsTransaction = QPaymentsTransaction.paymentsTransaction;

        BooleanExpression booleanExpression = paymentsTransaction.transactionDate.eq(cancelRequestDTO.getOriginalApprovalDate())
                .and(paymentsTransaction.cardNumber.eq(cancelRequestDTO.getCardNumber()))
                .and(paymentsTransaction.approveNumber.eq(cancelRequestDTO.getOriginalApprovalNumber()))
                .and(paymentsTransaction.businessNumber.eq(cancelRequestDTO.getBusinessNumber()))
                .and(paymentsTransaction.totalAmount.eq(cancelRequestDTO.getTotalAmount()))
                .and(paymentsTransaction.installmentsMonths.eq(cancelRequestDTO.getInstallmentsMonths()));

        return queryFactory.selectFrom(paymentsTransaction).where(booleanExpression).fetchOne();
    }

    // 쿼리 메서드로 findTransaction을 대체하여 JPARepository의 쿼리 메서드 기능을 활용하도록 할 수 있습니다.
    List<PaymentsTransaction> findByApprovalDateAndCardNumberAndApproveNumberAndBusinessNumberAndTotalAmountAndInstallmentsMonths(
            LocalDate approvalDate,
            String cardNumber,
            String approveNumber,
            String businessNumber,
            Long totalAmount,
            Integer installmentsMonths
    );
}