package com.example.vitabuddy.service;

import com.example.vitabuddy.dao.ICartListDAO;
import com.example.vitabuddy.dao.OrderInfoDAO;
import com.example.vitabuddy.model.OrderInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PaymentService {

    private static final Logger logger = Logger.getLogger(PaymentService.class.getName());

    @Value("${toss.SECRET_KEY}")
    private String secretKey;

    @Autowired
    @Qualifier("ICartListDAO")
    private ICartListDAO dao;

    private final OrderInfoDAO orderInfoDAO;

    @Autowired
    public PaymentService(OrderInfoDAO orderInfoDAO) {
        this.orderInfoDAO = orderInfoDAO;
    }

    public String approvePayment(String paymentKey, String orderId, Long amount) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);
        requestBody.put("amount", amount);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, ""); // 여기에 주입된 secretKey 사용

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            logger.info("결제 승인 성공: " + response.getBody());
            return response.getBody();
        } catch (Exception e) {
            logger.severe("결제 승인 실패: " + e.getMessage());
            return "결제 승인 실패: " + e.getMessage();
        }
    }

    public void saveOrderInfo(OrderInfoVO orderInfo) {
        logger.info("DB에 주문 정보 저장: " + orderInfo);
        orderInfoDAO.insertOrderInfo(orderInfo);
        logger.info("DB 저장 완료");
        HashMap<String, Object> map = new HashMap<>();
        map.put("orderId", orderInfo.getOrderId());
        map.put("userId", orderInfo.getUserId());
        dao.insertOrderProduct(map);
        dao.deleteCartAfterOrder(orderInfo.getUserId());
    }
}
