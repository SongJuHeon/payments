package com.example.payments.payment.repository;

import com.example.payments.payment.entity.PaymentsTransaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentsInfoRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(PaymentsTransaction paymentsTransaction) {
        em.persist(paymentsTransaction);
    }

    public PaymentsTransaction fineOne(String id) {
        return em.find(PaymentsTransaction.class, id);
    }

    public List<PaymentsTransaction> findTransaction(String approvalDate, String cardNumber) {
        return em.createQuery("select p from PaymentsInfo p where p.approvalDate = :approvalDate" +
                "and p.cardNumber = :cardNumber")
                .setParameter("approvalDate", approvalDate)
                .setParameter("cardNumber", cardNumber)
                .getResultList();
    }
}
