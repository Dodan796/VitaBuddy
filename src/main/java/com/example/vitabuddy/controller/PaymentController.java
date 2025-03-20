package com.example.vitabuddy.controller;

import com.example.vitabuddy.model.OrderInfoVO;
import com.example.vitabuddy.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // 세션에 OrderInfo 저장
    @PostMapping("/session")
    public ResponseEntity<String> saveOrderInfoToSession(
            @RequestBody OrderInfoVO orderInfo,
            HttpSession session
    ) {
        session.setAttribute("orderInfo", orderInfo);
        return ResponseEntity.ok("OrderInfo saved in session");
    }

    // 결제 성공 처리 (GET 요청도 허용)
    @RequestMapping(value = "/success", method = {RequestMethod.GET, RequestMethod.POST})
    public String successPayment(
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("orderId") String orderId,
            @RequestParam("amount") Long amount,
            HttpSession session,
            Model model
    ) {
        // 승인 API 호출
        String result = paymentService.approvePayment(paymentKey, orderId, amount);
        if (result.startsWith("결제 승인 실패")) {
            model.addAttribute("error", result);
            return "supplement/orderForm"; // 실패 시 주문서 페이지로 이동
        }

        // 세션에서 OrderInfo 가져오기
        OrderInfoVO orderInfo = (OrderInfoVO) session.getAttribute("orderInfo");
        if (orderInfo == null) {
            model.addAttribute("error", "세션에서 주문 정보를 찾을 수 없습니다.");
            return "supplement/orderForm";
        }

        // DB에 주문 정보 저장
        orderInfo.setPaymentKey(paymentKey);
        orderInfo.setOrderId(orderId);
        orderInfo.setOrdPay("CARD");
        paymentService.saveOrderInfo(orderInfo);

        model.addAttribute("message", "결제가 성공적으로 완료되었습니다.");
        return "supplement/orderComplete"; // 성공 시 주문 완료 페이지로 이동
    }

    @GetMapping("/fail")
    public String failPayment(Model model) {
        model.addAttribute("error", "결제가 실패하였습니다. 다시 시도해주세요.");
        return "supplement/orderForm"; // 실패 시 주문서 페이지 유지
    }
}
