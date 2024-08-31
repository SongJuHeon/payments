package com.example.payments.payment.service;

import com.example.payments.payment.entity.PaymentsTransaction;
import com.example.payments.payment.repository.PaymentsInfoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentsInfoService {

    @Autowired
    private final PaymentsInfoRepository paymentsInfoRepository;

    // 요청정보 저장
    // 요청정보 검증 -> 추후 기능 업데이트
    // PG 서비스 개발

    /// 승인요청 - 인자값 확정 필요
    @Transactional
    public Object approveRequest(Object object) {
        // 1. 트랜잭션 초기화
        PaymentsTransaction transaction = PaymentsTransaction.createTransaction(object);
        // 1. 요청정보 저장
        paymentsInfoRepository.save(transaction);
        // 2. 검증 로직. 아래 내용들은 도메인에서??
        //   2-1. 유효한 가맹점인지
        //   2-2. 유효한 카드 정보인지 - 유효기간, 카드 유효성
        // 3. 승인번호 생성
        // 4. 승인 결과 업데이트
        transaction.approve();
        // 결제성공 정보 반환 - 거래번호, 승인일자, 시간, 금액, 승인번호, 할부개월
        return transaction;
    }

    /// 취소요청
    public String cancelRequest(PaymentsTransaction paymentsTransaction) {

        return "1111";
    }
}
