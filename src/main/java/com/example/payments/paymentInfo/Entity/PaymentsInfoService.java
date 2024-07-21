package com.example.payments.paymentInfo.Entity;

import com.example.payments.paymentInfo.repository.PaymentsInfoRepository;
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
    // 내가 만들고 싶은것이 카드사 서비스인가 PG서비스인가.
    // 카드사 서비스라 하면 요청 인입 받고나서 유효성 검증 과정에서 실패하면 저장?
    // 아니면 일단 저장 후 검증 과정에서 실패시 실패정보 업데이트, 성공하면 성공정보 업데이트 후 전송?

    /// 일단 가장 간단하게 최소한의 정보만 확인해서 맞으면 승인 내주는 서비스로 만들고 추후 규모를 키우도록 하자.

    // 요청정보 전달 -> 다른 서버로 전달해야 하나 일단 정상 승인 받았다는 가정하에 진행
    // 요청정보 수신 -> 다른 서버로 전달해야 하나 일단 정상 승인 받았다는 가정하에 진행
    // 요청정보 업데이트

    // 요청정보 저장
    @Transactional
    public String insertRequest(PaymentsInfo paymentsInfo) {
        paymentsInfoRepository.save(paymentsInfo);
        /// 요청정보 검증 롤 수행
        /// 카드번호 유효성 검증, 유효기간 검증, 금액 검증
        /// 오류가 나면 오류 난 시점에서 DB 업데이트
        /// 정상적이면 정상처리 DB 업데이트
        return paymentsInfo.getOrderNumber();
    }

}
