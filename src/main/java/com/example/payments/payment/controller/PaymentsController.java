package com.example.payments.payment.controller;

import com.example.payments.payment.service.PaymentsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentsService paymentsService;

    @GetMapping("/payments/pay")
    public String paymentsForm(Model model) {
        model.addAttribute("paymentsRequest", new PaymentsRequest());
        return "/payments/payForm";
    }

    @PostMapping("/payments/pay")
    public String requestPayment(@Valid PaymentsRequest paymentsRequest, BindingResult result) {

        if (result.hasErrors()) {
            return "/payments/payForm";
        }

        /// 사업자번호, 단말기번호는 승인, 취소에 집중하기 위해 일시적으로 하드코딩, 넘길 자료형 리팩토링 필요
        paymentsService.approveRequest(paymentsRequest.getCardNumber()
                , paymentsRequest.getExpirationDate(), paymentsRequest.getAmount()
                , paymentsRequest.getInstallmentMonths(), "1212121212"
                , "2400000001");

        return "redirect:/";
    }
}
