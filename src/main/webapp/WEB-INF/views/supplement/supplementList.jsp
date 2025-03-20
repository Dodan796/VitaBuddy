<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 목록</title>

<c:import url="/WEB-INF/views/layout/head.jsp" />    
<script src="<c:url value='/js/jquery-3.7.1.min.js'/>"></script>

<script src="<c:url value='/js/category.js'/>"></script>
<script src="<c:url value='/js/search.js'/>"></script> <!-- 검색 js 수정 -->
<script src="<c:url value='/js/pageSupplementList.js'/>"></script> <!-- 페이지 js 추가 -->
<link rel="stylesheet" type="text/css" href="<c:url value='/css/supplementList.css'/>"> <!-- css 링크 수정 -->

</head>
<body>
<div id="wrap">
<c:import url="/WEB-INF/views/layout/top.jsp" />

	<section>
		<!-- 기본 상점 페이지로 돌아가는 url; 로고를 누르면 상점 페이지 초기화되도록 -->
	    <a href="<c:url value='/supplement/supplementList'/>"><h1>FIND YOUR NUTRITION</h1></a>
	    <br>
	    <hr>

	    <!-- 검색창 -->
	    <div class="search">
	        <form class="searchBox" id="searchForm" method="get">
	            <input type="text" name="keyword" id="keyword" class="searchTxt" placeholder="Search">
	            <button type="submit" class="searchBtn">
	                <i class="fa-solid fa-magnifying-glass"></i>
	            </button>
	        </form>
	    </div>  
	</section>

	<nav>
	<!-- 상위 카테고리 -->
	<div id="category">
	    <ul>
	        <li data-category="functions"><a href="#">기능별</a></li>  <!-- 수정사항1. function 을 functions로 바꿈 s 추가 (이하동일) -->
	        <li data-category="ingredients"><a href="#">성분별</a></li>
	        <li data-category="brands"><a href="#">브랜드별</a></li>
	    </ul>
	</div>


	<!-- 하위 카테고리 -->
	<div id="subCtg">
	    <div class="subCtgMenu" data-category="functions">
	        <ul>

	            <!-- [기능별] 하위 카테고리 출력 부분 : ajax 통신으로 진행 -->

	        </ul>
	    </div>
	    <div class="subCtgMenu" data-category="ingredients">
	        <ul>
	            <!-- [성분별] 하위 카테고리 출력 부분 : ajax 통신으로 진행 -->

	        </ul>
	    </div>
	    <div class="subCtgMenu" data-category="brands">
	        <ul>
	           <!-- [브랜드별] 하위 카테고리 출력 부분 : ajax 통신으로 진행 -->
	        </ul>
	    </div>
	</div>


	<!-- 정렬 -->
	    <div class="ordNav">
	        <select id="order">
	            <option value="review">리뷰순</option>
	            <option value="sales">구매순</option>
	        </select>
	    </div> 
	</nav>      



	<section>
	    <!-- 상품 데이터 반복 출력 -->  
	    <div class="products">

	    	<!-- 전체 상품  출력 -->  
	    	<c:forEach var="sup" items="${pagingsupList}">
	           <div class="productItem">
	           <a href="<c:url value='/api/supplement/supplementDetail/${sup.supId}'/>">
	           <img class="prdImg" src="data:image/png;base64,${sup.base64SupImg}" width="300" height="300"> </a>
		        <p>${sup.supName}</p>
		        <p><fmt:setLocale value="ko_KR"/><fmt:formatNumber type="currency" value="${sup.supPrice}"/></p>  <!-- 한국 통화 표시 -->
		        <p>${sup.supBrand}</p>
	           </div>
	        </c:forEach>


	        <!-- brand 태그 선택시 상품 출력 -->  <!-- 막상 해시태그값을 선택했을 때 상품이 나오는 js파일은 없다. 그냥 jsp로 -->
	        <c:forEach var="brandsup" items="${pagingbrandsupList}">
	           <div class="productItem">
	           <a href="<c:url value='/api/supplement/supplementDetail/${brandsup.supId}'/>">   <!-- 상품 상세 연결 링크 수정 -->
	           <img class="prdImg" src="data:image/png;base64,${brandsup.base64SupImg}" width="300" height="300"> </a>  <!-- 상품 이미지 출력 코드 : 클릭했을 때, 해당 상품의 supId로 넘어간다 (제품 상세페이지로 이동) -->
		        <p>${brandsup.supName}</p>
		        <p><fmt:setLocale value="ko_KR"/><fmt:formatNumber type="currency" value="${brandsup.supPrice}"/></p>
		        <p>${brandsup.supBrand}</p>
	           </div>
	        </c:forEach> 

	        <!-- function 태그 선택시 상품 출력 -->
	        <c:forEach var="funcsup" items="${pagingfunctionsupList}">
	           <div class="productItem">
	  		   <a href="<c:url value='/api/supplement/supplementDetail/${funcsup.supId}'/>">   <!-- 상품 상세 연결 링크 수정 -->
	           <img class="prdImg" src="data:image/png;base64,${funcsup.base64SupImg}" width="300" height="300"> </a>  <!-- 상품 이미지 출력 코드 : 클릭했을 때, 해당 상품의 supId로 넘어간다 (제품 상세페이지로 이동) -->
		        <p>${funcsup.supName}</p>
		        <p><fmt:setLocale value="ko_KR"/><fmt:formatNumber type="currency" value="${funcsup.supPrice}"/></p>
		        <p>${funcsup.supBrand}</p>
	           </div>
	        </c:forEach>


	        <!-- ingredients 태그 선택시 상품 출력 -->
	        <c:forEach var="ingresup" items="${pagingingredientsupList}">
	           <div class="productItem">
	           <a href="<c:url value='/api/supplement/supplementDetail/${ingresup.supId}'/>">   <!-- 상품 상세 연결 링크 수정 -->
	           <img class="prdImg" src="data:image/png;base64,${ingresup.base64SupImg}" width="300" height="300"> </a>  <!-- 상품 이미지 출력 코드 : 클릭했을 때, 해당 상품의 supId로 넘어간다 (제품 상세페이지로 이동) -->
		        <p>${ingresup.supName}</p>
		        <p><fmt:setLocale value="ko_KR"/><fmt:formatNumber type="currency" value="${ingresup.supPrice}"/></p>
		        <p>${ingresup.supBrand}</p>
	           </div>
	        </c:forEach>


	        <%-- <div class="productItem">
	            <a href="<c:url value='/product/detailView/${sup.supNo}'/>">
	            <img src="sup.supImg" alt="${sup.supName}">
		        </a>
		        <p>sup.supName</p>
		        <p>sup.supPrice</p>
		        <p>sup.supBrand</p>
	           </div> --%>

		</div>

	</section>        

	<nav>

	    <!-- 페이지네이션 -->
	    <!-- a 태그 button으로 수정 1017 페이지네이션 수정 -->
		    <%-- <div class="pagination">
			    <!-- 이전 버튼 -->
			    <button class="prev <c:if test='${currentPage == 1}'>disabled</c:if>" data-page="${currentPage - 1}" onClick="goToPage(${currentPage - 1})">
			        <i class="fa-solid fa-caret-left"></i>
			    </button>
			
			    <!-- 페이지 번호 버튼 -->
			    <c:forEach var="i" begin="1" end="${totalPages}">
			        <button class="page <c:if test='${i == currentPage}'> active</c:if>" onClick="goToPage(${i})" data-page="${i}">${i}</button>
			    </c:forEach>
			
			    <!-- 다음 버튼 -->
			    <button class="next <c:if test='${currentPage == totalPages}'>disabled</c:if>" data-page="${currentPage + 1}" onClick="goToPage(${currentPage + 1})">
			        <i class="fa-solid fa-caret-right"></i>
			    </button>
			</div> --%>
			<c:set var="startPage" value="${currentPage - 2}" />
			<c:set var="endPage" value="${currentPage + 2}" />

			<!-- 시작 페이지와 끝 페이지 조정 -->
			<c:if test="${startPage < 1}">
			    <c:set var="startPage" value="1" />
			    <c:set var="endPage" value="5" />
			</c:if>

			<c:if test="${endPage > totalPages}">
			    <c:set var="endPage" value="${totalPages}" />
			    <c:set var="startPage" value="${totalPages - 4}" />
			</c:if>

			<!-- 페이지가 5개보다 적을 때 시작 페이지 조정 -->
			<c:if test="${startPage < 1}">
			    <c:set var="startPage" value="1" />
			</c:if>

			<div class="pagination">
			    <!-- 이전 버튼 -->
			    <button class="prev <c:if test='${currentPage == 1}'>disabled</c:if>" data-page="${currentPage - 1}" onClick="goToPage(${currentPage - 1})">
			        <i class="fa-solid fa-caret-left"></i>
			    </button>

			    <!-- 페이지 번호 버튼 -->
			    <c:forEach var="i" begin="${startPage}" end="${endPage}">
			        <button class="page <c:if test='${i == currentPage}'> active</c:if>" onClick="goToPage(${i})" data-page="${i}">${i}</button>
			    </c:forEach>

			    <!-- 다음 버튼 -->
			    <button class="next <c:if test='${currentPage == totalPages}'>disabled</c:if>" data-page="${currentPage + 1}" onClick="goToPage(${currentPage + 1})">
			        <i class="fa-solid fa-caret-right"></i>
			    </button>
			</div>



	</nav> 


<!--  footer -->         
<c:import url="/WEB-INF/views/layout/footer.jsp" /> 
</div>    
</body>
</html>