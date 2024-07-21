package com.example.payments.paymentInfo.repository;

import com.example.payments.paymentInfo.Entity.PaymentsInfo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentsInfoRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(PaymentsInfo paymentsInfo) {
        em.persist(paymentsInfo);
    }

    public PaymentsInfo fineOne(String id) {
        return em.find(PaymentsInfo.class, id);
    }

    public List<PaymentsInfo> findTransaction(String approvalDate, String cardNumber) {
        return em.createQuery("select p from PaymentsInfo p where p.approvalDate = :approvalDate" +
                "and p.cardNumber = :cardNumber")
                .setParameter("approvalDate", approvalDate)
                .setParameter("cardNumber", cardNumber)
                .getResultList();
    }
}
