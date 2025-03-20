<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="<c:url value='/js/login.js'/>" defer></script>

<header>
	<div id="headerBox">
		<div id="logoBox">
            <a href="<c:url value='/'/>"><img src="<c:url value='/image/logo.png'/>" id="logoImg"  width="240" height="80"></a>
            <%-- <a href="<c:url value='/home'/>">VITABUDDY</a> --%>
        </div>
		<div id="topMenu">			
			<!-- 로그인 및 회원가입, 쿠키에서 userRole과 userId값을 가져옴 -->
			<c:set var="userRole" value="${cookie.userRole != null ? cookie.userRole.value : null}" />
            <c:set var="userId" value="${cookie.userId != null ? cookie.userId.value : null}" />

            <!--kakao로그인 실패 시 || 비회원 (userRole이 null일 때) -->
            <c:if test="${empty sessionScope.sid and userRole == null}" >  <!-- or >> and 로 수정-->
				<a href="<c:url value='/intro'/>">로그인</a>
				<a href="<c:url value='/member/register'/>">회원가입</a>
			</c:if>

            <!-- 로그인 성공 -->
			<!-- 1. 카카오 소셜 로그인 -->
            <c:if test="${not empty sessionScope.sid}"> <!--kakao로 로그인 되었을 경우 -->
                <a href="<c:url value='/member/logout'/>">로그아웃</a>
                <a href="<c:url value='/supplement/wishList'/>"><i
                    class="fas fa-solid fa-heart"></i></a>
                <a href="<c:url value='/supplement/cartList'/>"><i
                    class="fa-solid fa-cart-shopping"></i></a>
                <a href="<c:url value='/member/myPage'/>"><i
                    class="fa-solid fa-user"></i></a>
			</c:if>

			<!-- 2. JWT 로그인 -->
			<!-- 2-1 일반 회원 -->
            <c:if test="${userRole == 'ROLE_USER' && cookie.userId != null}">
               <a href="/logout" id="logoutButton">로그아웃</a>
               <a href="<c:url value='/supplement/wishList'/>">
                    <i class="fas fa-solid fa-heart"></i>
               </a>
               <a href="<c:url value='/supplement/cartList'/>">
                    <i class="fa-solid fa-cart-shopping"></i>
               </a>
               <a href="<c:url value='/member/myPage'/>">
                    <i class="fa-solid fa-user"></i>
               </a>
            </c:if>
		</div>
	</div>
	<hr>
</header>
