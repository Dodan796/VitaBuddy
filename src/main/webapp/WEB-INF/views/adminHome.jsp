<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>VitaBuddy</title>
 	<link rel="stylesheet" type="text/css" href="<c:url value='/css/home.css'/>">
 	<script src="<c:url value='/js/home.js'/>" defer></script>
	<c:import url="/WEB-INF/views/layout/head.jsp" />
</head>
<body>
<div id="wrap">
<c:import url="/WEB-INF/views/layout/top.jsp" />

	<h1>VITABUDDY</h1>

	<!-- 검색창 -->
	<div class="search">
	<form class="searchBox" id="searchForm" method="get" action="<c:url value='/supplement/supplementList'/>">
			<input type="text" name="keyword" id="keyword" class="searchTxt" placeholder="Search">
			<button type="submit" class="searchBtn">  <!--돋보기 아이콘  -->
			<i class="fa-solid fa-magnifying-glass"></i>
			</button>
		</form>
	</div>


	<div class="title"><strong>건강한 영양제 복용을 위한 파트너!</strong><br><br>맞춤 성분 추천과 올바른 복용법 가이드를 통해 더 쉽게 건강을 관리하세요.
	<div class="background-text">VitaBuddy</div></div>
	<br>

<section class="home">
	<!-- 메인메뉴 -->
	<div class="mainMenu">

	<!-- 로그인 하지 않은 경우 -->
		<c:if test="${empty sessionScope.sid }">
		<a href="<c:url value='/supplement/supplementList'/>"><img src="<c:url value='/image/prdList.png'/>" class="menuImg"><br>상점</a>
		</c:if>

	<!-- 로그인 한 경우 -->
		<c:if test="${not empty sessionScope.sid }">
		<a href="<c:url value='/supplement/supplementList'/>"><img src="<c:url value='/image/prdList.png'/>" class="menuImg"><br>상점</a>
		<a href="<c:url value='/member/myPage'/>"><img src="<c:url value='/image/myPage.png'/>" class="menuImg"><br>마이페이지</a>
		</c:if>
	</div>
</section>
<br><br>
<section class="home item">
    <h2>인기상품</h2>

    <!-- 카테고리 버튼 -->
		<div class="category-buttons">
		    <button data-category="brand" onclick="selectCategory('brand')" class="category-button"><h3>브랜드별 인기상품</h3></button>
		    <button data-category="function" onclick="selectCategory('function')" class="category-button"><h3>기능별 인기상품</h3></button>
		    <button data-category="ingredient" onclick="selectCategory('ingredient')" class="category-button"><h3>성분별 인기상품</h3></button>
		</div>

    <!-- 브랜드별 인기상품 -->
    <div id="brand" class="bestItems category-section" style="display: grid;" data-items='${topSupplementsBrand}'>
        <c:forEach var="sup" items="${topSupplementsBrand}" varStatus="status">
            <div class="bestItem" style="<c:if test='${status.index >= 6}'>display:none;</c:if>">
                <a href="<c:url value='/api/supplement/supplementDetail/${sup.supId}'/>">
                    <img class="prdImg" src="data:image/png;base64,${sup.base64SupImg}" alt="${sup.supName}" width="200" height="200">
                </a>
                <p title="${fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))}">
				    <c:choose>
				        <c:when test="${fn:length(fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))) > 13}">
				            ${fn:substring(fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', '')), 0, 13)}...
				        </c:when>
				        <c:otherwise>
				            ${fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))}
				        </c:otherwise>
				    </c:choose>
				</p>
                <p>${sup.supBrand}</p>
                <p>₩ <fmt:formatNumber value="${sup.supPrice}" pattern="#,###" /><!-- 원 --></p>
            </div>
        </c:forEach>
    </div>

    <!-- 기능별 인기상품 -->
    <div id="function" class="bestItems category-section" style="display:none;" data-items='${topSupplementsFunction}'>
        <c:forEach var="sup" items="${topSupplementsFunction}" varStatus="status">
            <div class="bestItem" style="<c:if test='${status.index >= 6}'>display:none;</c:if>">
                <a href="<c:url value='/api/supplement/supplementDetail/${sup.supId}'/>">
                    <img class="prdImg" src="data:image/png;base64,${sup.base64SupImg}" alt="${sup.supName}" width="200" height="200">
                </a>
                <p title="${fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))}">
				    <c:choose>
				        <c:when test="${fn:length(fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))) > 13}">
				            ${fn:substring(fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', '')), 0, 13)}...
				        </c:when>
				        <c:otherwise>
				            ${fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))}
				        </c:otherwise>
				    </c:choose>
				</p>
                <p>${sup.supBrand}</p>
                <p>₩ <fmt:formatNumber value="${sup.supPrice}" pattern="#,###" /> <!-- 원 --></p>
            </div>
        </c:forEach>
    </div>

    <!-- 성분별 인기상품 -->
    <div id="ingredient" class="bestItems category-section" style="display:none;" data-items='${topSupplementsIngredient}'>
        <c:forEach var="sup" items="${topSupplementsIngredient}" varStatus="status">
            <div class="bestItem" style="<c:if test='${status.index >= 6}'>display:none;</c:if>">
                <a href="<c:url value='/api/supplement/supplementDetail/${sup.supId}'/>">
                    <img class="prdImg" src="data:image/png;base64,${sup.base64SupImg}" alt="${sup.supName}" width="200" height="200">
                </a>
                <p title="${fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))}">
				    <c:choose>
				        <c:when test="${fn:length(fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))) > 13}">
				            ${fn:substring(fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', '')), 0, 13)}...
				        </c:when>
				        <c:otherwise>
				            ${fn:trim(fn:replace(fn:substringAfter(sup.supName, sup.supBrand), ',', ''))}
				        </c:otherwise>
				    </c:choose>
				</p>
                <p>${sup.supBrand}</p>
                <p>₩ <fmt:formatNumber value="${sup.supPrice}" pattern="#,###" /><!-- 원 --></p>
            </div>
        </c:forEach>
    </div>

    <!-- 페이지네이션 -->
    <div class="pagination" id="pagination" style="display:none;">
        <button class="prev" onclick="goToPage('prev')"><i class="fa-solid fa-caret-left"></i></button>
        <span id="page-indicator"></span>
        <button class="next" onclick="goToPage('next')"><i class="fa-solid fa-caret-right"></i></button>
    </div>
</section>


<c:import url="/WEB-INF/views/layout/footer.jsp" />

</div>
</body>
</html>