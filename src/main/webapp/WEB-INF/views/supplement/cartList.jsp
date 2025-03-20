	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>장바구니</title>
    <c:import url="/WEB-INF/views/layout/head.jsp" />  
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/cartList.css'/>">
    <script src="<c:url value='/js/ordPrice.js' />"></script>
    <script src="<c:url value='/js/deleteCart.js' />"></script>    
</head>
<body>
    <div id="wrap">
        <!-- top -->
        <c:import url="/WEB-INF/views/layout/top.jsp" />
        <section>        
            <div class="cartContainer">
                <h1>장바구니</h1>
                <!-- 결제하기 누르면 바로 주문정보 창 -->
                <form method="post" action="<c:url value='/supplement/orderForm'/>">
                    <table class="cartItems">
                        <thead>
                            <tr>
                                <th class="cartNo">번호</th>
                                <th colspan="2">상품 정보</th>
                                <th class="supPrice">가격</th>
                                <th class="ordPrice" colspan="2">주문금액</th>
                            </tr>
                        </thead>
                        <!-- 장바구니가 빈 경우 -->
                    	<c:choose>
                            <c:when test="${empty cartLists}">
                                <tbody>
                                    <tr><td colspan="6">장바구니가 비어있습니다.</td></tr>
                                </tbody>
                            </c:when>
                            <c:otherwise>
                            <!-- 장바구니에 상품이 있을 경우 -->
                                <c:forEach var="cartList" items="${cartLists}" varStatus="status"> 
                                    <tbody>
                                        <tr>
                                            <td rowspan="3">${status.index + 1}</td> <!-- cartId로 하면 cart고유값이 그대로 노출된다 (cartId를 사용할 경우, 1, 2, 3 중에서 2번을 삭제했다고 하면, 화면에 1, 3 이라고 뜸) >> status.index를 사용하도록 함 -->
                                            <td rowspan="3" class="tableImg">
                                            <!-- sup.supId>cartList.cartList로 수정 -->
                                            <!-- cart로 된 모든 변수 cartList로 수정 -->
                                                <a href="<c:url value='/api/supplement/supplementDetail/${cartList.supId}'/>">  
                                                    <img class="supImg" src="data:image/png;base64,${cartList.base64SupImg}">  <!-- 1024 수정사항 : ${supImgBase64} -> ${cartList.base64SupImg} 로 수정 -->
                                                </a>
                                            </td>              
                                            <td class="supDetail">${cartList.supName}</td>
                                            <td>
                                                ₩ <span class="price" data-price="${cartList.supPrice}">
                                                    <fmt:formatNumber value="${cartList.supPrice}" pattern="#,###" />
                                                </span>
                                            </td>
                                            <td>수량 : 
                                                <input type="button" class="minusBtn" value="-"> 
												<input type="text" class="cartQty" name="cartQty" value="${cartList.cartQty}"  size="1" readonly>   <!-- 1023수정사항 : DB에서 cartQty를 가지고 옴-->
												<input type="button" class="plusBtn" value="+"> 
                                            </td>
                                            <td rowspan="3" class="deleteBtn">
                                                <input type="hidden" name="supId" value="${cartList.supId}">  
                                          <!-- 버튼에 데이터 속성 추가 -->
                                                <button class="deleteCartBtn" type="button" data-sup-id="${cartList.supId}" data-cart-id="${cartList.cartId}" data-user-id="${sessionScope.sid}" value="삭제">
												    <i class="fa-solid fa-trash"></i>
												</button>

                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="supDetail">${cartList.supBrand}</td><td></td><td></td>
                                        </tr>
                                        <tr>
                                            <td></td><td></td>
                                            <td>₩ <span class="amount"><fmt:formatNumber value="${cartList.supPrice}" pattern="#,###" /></span></td>
                                        </tr>
                                    </tbody>
                                  </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </table>
                    
                    <!-- 총액 표시 및 결제 버튼 -->
                    <div class="totalResult">
                        주문 내역
                        <br>
                        상품 총액: ₩ <span id="total">
                            <!-- 총 구매 예정 금액 표시 -->
                            <fmt:formatNumber value="${sum}" pattern="#,###"/></span>
                        <br>
                        <!-- 결제하기 버튼 -->
                        <input type="submit" value="결제하기" class="btn btnFilled">
                    </div>
                </form> 
            </div>
        </section>
        
        <!-- footer -->
        <c:import url="/WEB-INF/views/layout/footer.jsp" />
    </div>
</body>
</html>