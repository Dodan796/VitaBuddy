<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/myPage.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/head.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/footer.css'/>">
<script src="/js/jquery-3.7.1.min.js"></script>
<script src="/js/purchaseList.js"></script>
<c:import url="/WEB-INF/views/layout/head.jsp"/>
<meta charset="UTF-8">
<title>마이페이지</title>
</head>
<body>
<c:import url="/WEB-INF/views/layout/top.jsp" />
    <section id="wrap">
        <div class="container">
            <div class="headers">
                <h1>마이페이지</h1>
            </div>
            <div class="box">
            	<div class="tooltip-container">
				  <span class="tooltip-icon"><i class="fa-solid fa-circle-question"></i></span>
				  <div class="tooltip-text">비타버디에서 판매 중인 상품들의 주성분을 기준으로 좋은 시너지가 나는 영양제 성분을 추천해주는 동시에 안전한 복용법을 지도합니다. <br>
				  <strong>-마이페이지에서 추천 성분과 주의사항을 확인하려면?</strong><br>
						회원 정보 수정 페이지에서 현재 복용 중인 영양제를 추가해 보세요. 추가된 영양제를 바탕으로 시너지를 내는 추천 성분과 상호작용, 주의사항을 마이페이지에서 확인하실 수 있습니다.</div>
				</div>
                <p>복용 중인 영양제</p>
                <div class="mySupplement">
                    <c:forEach var="supplement" items="${userSupplements}" varStatus="status">
                        <div class="supplement-item">
                            <span>${fn:trim(supplement.supName)} - ${fn:trim(supplement.supBrand)}</span>
                        </div>
                    </c:forEach>
                </div>

                <div class="box_rowContents">
                    <div class="horizontal_box">
						<p>추천 성분</p>
                        <div class="recommend">
                            <div class="recommendList">
                                <c:forEach var="entry" items="${allRecommendLists}">
                                    <b>${entry.key + 1}.</b>
                                    <c:choose>
                                        <c:when test="${not empty entry.value}">
                                            <c:forEach var="recommendVO" items="${entry.value}">
                                                <b>추천 성분</b>: ${recommendVO.ingredients} <br>
                                                <b>영양제 궁합</b>: ${recommendVO.interactionRecommend} <br>


                                                <%-- <c:if test="${topProductsByIngredient[recommendVO.ingredientId] != null}">
                                        <a href="/supplement/supplementDetail/${topProductsByIngredient[recommendVO.ingredientId].supId}">
                                            ${topProductsByIngredient[recommendVO.ingredientId].supName}
                                        </a>
                                    </c:if> --%>
                                                <!-- 추천 성분에 따른 상위 제품 링크 추가 -->
                                    <c:if test="${topProductsByIngredient != null}">

                                    <c:forEach var="topPrd" items="${topProductsByIngredient}">
                                       <a href="<c:url value='/api/supplement/supplementDetail/${topPrd.value.supId}'/>">
                                         ${topPrd.value.supName}
                                         </a>
                                         <br>
                                    </c:forEach>
                                    </c:if>
                                                <br><br>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <b>추천 성분</b>: [ ] <br><br>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <div class="horizontal_box">
                        <p>성분간 상호작용</p>
                        <div class="interaction">
                            <c:choose>
                                <c:when test="${not empty interactions}">
                                    <c:forEach var="interaction" items="${interactions}" varStatus="status">
                                        <c:if test="${interaction.ingredient2 != '해당 없음' && interaction.interactionDetail != '상호작용 정보 없음'}">
                                            <div class="interactionList">
                                                <li>
                                                    <strong>[${interaction.ingredient1} - ${interaction.ingredient2}]</strong><br>
                                                    <b>문제</b>: ${fn:trim(interaction.interactionDetail)}<br><br>
                                                    <b>올바른 복용법</b>: ${fn:trim(interaction.interactionDosage)}
                                                </li>
                                            </div>
                                        </c:if>
                                    </c:forEach>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>

            <button class="submit-btn" onclick="location.href='/member/myInfoChangeForm?userId=${sessionScope.sid}'">회원 정보/ 영양제 관리</button>
 <!-- 내가 작성한 리뷰 목록 -->
                    <div class="review-section">
                        <h3>내가 작성한 리뷰 목록</h3>
                        <form action="/myPage/reviews" method="get">
                            <label for="sort">정렬 기준:</label>
                            <select name="sort" id="sort" onchange="this.form.submit()">
                                <option value="recent">최신순</option>
                                <option value="oldest">오래된 순</option>
                            </select>
                        </form>
                        <table id="reviewList">
                            <thead>
                                <tr>
<!-- 10/21 th 클래스명 추가 -->
                                    <th class="myReviewNo">번호</th>
                                    <th class="important">제목</th>
                                    <th class="supName">상품명</th>
                                    <th class="reviewDate">작성일</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:choose>
                                    <c:when test="${not empty reviews}">
                                    <c:forEach var="review" items="${reviews}" varStatus="status">
                                        <tr>
                                            <td>${status.index + 1}</td> <!-- 리뷰 번호를 1부터 시작 -->
                                            <td class="important">${review.reviewTitle}</td>
                                            <td>${review.supName}</td> <!-- 상품명을 표시할 컬럼 -->
                                            <td><fmt:formatDate value="${review.reviewDate}" pattern="yyyy-MM-dd" /></td>
                                        </tr>
                                    </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="4" align="center">작성하신 리뷰가 없습니다.</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                    <!-- 구매 내역 : 10/25 myPage 구매내역 부분 수정 -->
  					<div class="purchase-section">
                        <h3>구매 내역</h3>

                        <!-- 10/29 수정 -->
                        <div class="filter-bar">
					        <label for="order">정렬 기준:</label>

					        <div class="right-options">
						        <div class="filter-options">
						            <label><input type="radio" name="dateRange" value="1month" onclick="showPurchases('recentPurchases')"> 1개월 전</label>
						            <label><input type="radio" name="dateRange" value="1to3months" onclick="showPurchases('midTermPurchases')"> 1~3개월 전</label>
						            <label><input type="radio" name="dateRange" value="3months" onclick="showPurchases('oldPurchases')"> 3개월 전</label>
						        </div>

						        <select name="order">
						            <option>현재 주문 처리 상태</option>
						            <option>주문 완료</option>
						            <option>배송 준비</option>
						            <option>배송 시작</option>
						            <option>배송 완료</option>
						        </select>
						    </div>
					    </div>
                        <!-- 10/29 수정 -->
						<!-- <br> --> <!-- 10/21 br 태그 추가 -->  
                        <table width="100%" id="productInfo">
                            <thead>
                                <tr>
                                    <th>주문일자</th>
                                    <th>이미지</th>
                                    <th>상품정보</th>
                                    <th>수량</th>
                                    <th>금액</th>
                                    <th>주문 처리</th>
                                </tr>
                            </thead>

                            	<!-- 1개월 전 구매내역 -->
                                <tbody id="recentPurchases" >
							        <c:choose>
							            <c:when test="${not empty recentPurchases}">
							                <c:forEach var="ord" items="${recentPurchases}" varStatus="status">
							                    <tr>
							                        <td><fmt:parseDate value="${ord.ordDate}" pattern="yyyy-MM-dd HH:mm:ss.SSS" var="parsedDate" />
							                        <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" /></td>
							                        <td><img src="data:image/png;base64,${ord.base64SupImg}" width="100" height="100"></td>
							                        <td>${ord.supName}</td>
							                        <td>${ord.ordQty}</td>
							                        <td><fmt:formatNumber value="${ord.supPrice * ord.ordQty}" pattern="#,###" /> 원</td>
							                        <td>배송 준비 중</td>
							                    </tr>
							                </c:forEach>
							            </c:when>
							            <c:otherwise>
							                <tr><td colspan="6" align="center">주문 내역이 없습니다.</td></tr>
							            </c:otherwise>
							        </c:choose>
							    </tbody>

							    <!-- 중기 구매 (1~3개월) -->
							    <tbody id="midTermPurchases" style="display:none;"> 
							        <c:choose>
							            <c:when test="${not empty midTermPurchases}">
							                <c:forEach var="ord" items="${midTermPurchases}" varStatus="status">
							                    <tr>
							                        <td><fmt:parseDate value="${ord.ordDate}" pattern="yyyy-MM-dd HH:mm:ss.SSS" var="parsedDate" />
                                                    <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" /></td>
							                        <td><img src="data:image/png;base64,${ord.base64SupImg}" width="100" height="100"></td>
							                        <td>${ord.supName}</td>
							                        <td>${ord.ordQty}</td>
							                        <td><fmt:formatNumber value="${ord.supPrice * ord.ordQty}" pattern="#,###" /> 원</td>
							                        <td>배송 준비 중</td>
							                    </tr>
							                </c:forEach>
							            </c:when>
							            <c:otherwise>
							                <tr><td colspan="6" align="center">주문 내역이 없습니다.</td></tr>
							            </c:otherwise>
							        </c:choose>
							    </tbody>

							    <!-- 오래된 구매 (3개월 이상) -->
							    <tbody id="oldPurchases" style="display:none;">
							        <c:choose>
							            <c:when test="${not empty oldPurchases}">
							                <c:forEach var="ord" items="${oldPurchases}" varStatus="status">
							                    <tr>
							                        <td><fmt:parseDate value="${ord.ordDate}" pattern="yyyy-MM-dd HH:mm:ss.SSS" var="parsedDate" />
                                                    <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd" /></td>
							                        <td><img src="data:image/png;base64,${ord.base64SupImg}" width="100" height="100"></td>
							                        <td>${ord.supName}</td>
							                        <td>${ord.ordQty}</td>
							                        <td><fmt:formatNumber value="${ord.supPrice * ord.ordQty}" pattern="#,###" /> 원</td>
							                        <td>배송 준비 중</td>
							                    </tr>
							                </c:forEach>
							            </c:when>
							            <c:otherwise>
							                <tr><td colspan="6" align="center">주문 내역이 없습니다.</td></tr>
							            </c:otherwise>
							        </c:choose>
							    </tbody>







                        </table>
                    </div>
                </div>
            </section>
    <c:import url="/WEB-INF/views/layout/footer.jsp"/>
</body>
</html>