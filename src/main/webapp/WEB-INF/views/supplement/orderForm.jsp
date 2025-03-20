<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주문서 작성</title>
    <!-- TossPayments SDK -->
    <script src="https://js.tosspayments.com/v2/standard"></script>
    <!-- 외부 JavaScript 파일 포함 -->
    <script src="<c:url value='/js/payment.js'/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/orderForm.css'/>">
    <c:import url="/WEB-INF/views/layout/head.jsp"/>
</head>
<body>
    <div id="wrap">
        <c:import url="/WEB-INF/views/layout/top.jsp" />
        <form id="orderForm">
            <section class="prdList">
                <h1>주문/결제</h1>
                <h3>주문 상품 목록</h3>
                <table class="productList">
                    <tr>
                        <th>번호</th>
                        <th colspan="2">상품</th>
                        <th>수량</th>
                        <th>가격</th>
                    </tr>
                    <c:set var="index" value="1"/>
                    <c:set var="totalAmount" value="0"/>
                    <c:forEach var="cartList" items="${cartLists}">
                        <tr>
                            <td>${index}</td>
                            <td class="supImgTd"><img class="supImg" src="data:image/png;base64,${cartList.base64SupImg}"></td>
                            <td class="supName">${cartList.supName}</td>
                            <td>${cartList.cartQty}</td>
                            <td>
                                ₩ <fmt:formatNumber value="${cartList.supPrice * cartList.cartQty}" pattern="#,###"/>
                                <c:set var="totalAmount" value="${totalAmount + cartList.supPrice * cartList.cartQty}"/>
                            </td>
                        </tr>
                        <c:set var="index" value="${index + 1}"/>
                    </c:forEach>
                    <tr>
                        <td colspan="4" align="right">총 금액 :</td>
                        <td>₩ <fmt:formatNumber value="${totalAmount}" pattern="#,###"/></td>
                    </tr>
                </table>
            </section>

            <section class="order">
                <h3>배송 정보</h3>
                <table class="ordRcv">
                    <tr>
                        <td>이름</td>
                        <td colspan="3"><input type="text" id="ordRcvReceiver" value="${myInfo.userName}" required/></td>
                    </tr>
                    <tr>
                        <td>전화번호</td>
                        <td colspan="3"><input type="text" id="ordRcvPhone" value="${myInfo.userPh}" required/></td>
                    </tr>
                    <tr>
                        <td>이메일</td>
                        <td colspan="3"><input type="email" id="ordRcvEmail" value="${myInfo.userEmail}" required/></td>
                    </tr>
                    <tr>
                        <td>우편번호</td>
                        <td colspan="3"><input type="text" id="ordRcvZipcode" value="${myInfo.userZipcode}" readonly/></td>
                    </tr>
                    <tr>
                        <td>주소</td>
                        <td colspan="3"><input type="text" id="ordRcvAddress1" value="${myInfo.userAddress1}" readonly/></td>
                    </tr>
                    <tr>
                        <td>상세주소</td>
                        <td colspan="3"><input type="text" id="ordRcvAddress2" value="${myInfo.userAddress2}"/></td>
                    </tr>
                    <tr>
                        <td>배송 메시지</td>
                        <td colspan="3"><textarea id="ordRcvMsg" rows="5" cols="50"></textarea></td>
                    </tr>
                </table>

                <!-- Hidden input에 userId를 추가하여 customerKey로 사용 -->
                <input type="hidden" id="userId" value="${myInfo.userId}">
                <input type="hidden" id="orderId" value="${orderId}">
                <input type="hidden" id="amount" value="${totalAmount}">
                <button class="submit-btn" type="button" id="submitBtn">결제하기</button>
            </section>
        </form>
        <c:import url="/WEB-INF/views/layout/footer.jsp"/>
    </div>

    <!-- 세션 저장 및 결제 요청 -->
    <script>
        document.getElementById('submitBtn').addEventListener('click', function () {
            const orderInfo = {
                userId: document.getElementById('userId').value,
                orderId: document.getElementById('orderId').value,
                ordRcvReceiver: document.getElementById('ordRcvReceiver').value,
                ordRcvPhone: document.getElementById('ordRcvPhone').value,
                ordRcvEmail: document.getElementById('ordRcvEmail').value,
                ordRcvZipcode: document.getElementById('ordRcvZipcode').value,
                ordRcvAddress1: document.getElementById('ordRcvAddress1').value,
                ordRcvAddress2: document.getElementById('ordRcvAddress2').value,
                ordRcvMsg: document.getElementById('ordRcvMsg').value,
            };

            // 세션 저장 요청
            fetch('/payment/session', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderInfo)
            }).then(response => response.json())
              .then(data => {
                  console.log('세션 저장 성공:', data);

                  // TossPayments 결제 요청
                  const tossPayments = TossPayments("test_ck_5OWRapdA8dwkboG6WZRAro1zEqZK");
                  tossPayments.requestPayment('카드', {
                      amount: document.getElementById('amount').value,
                      orderId: orderInfo.orderId,
                      orderName: "장바구니 상품 결제",
                      successUrl: `${window.location.origin}/payment/success`,
                      failUrl: `${window.location.origin}/payment/fail`,
                      customerName: orderInfo.ordRcvReceiver,
                      customerEmail: orderInfo.ordRcvEmail
                  });
              })
              .catch(error => console.error('세션 저장 실패:', error));
        });
    </script>
</body>
</html>
