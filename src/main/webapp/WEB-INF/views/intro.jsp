<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>

<c:import url="/WEB-INF/views/layout/head.jsp" />	
<link rel="stylesheet" type="text/css" href="<c:url value='/css/intro.css'/>">
<script src="<c:url value='/js/login.js'/>" defer></script>
<script src="<c:url value='/js/kakaoOAuth.js'/>"></script>

</head>
<body>
<div id="wrap">
	<!-- 인트로 이미지 -->
	<div class="introTitle">
		<img src="<c:url value='/image/mainImage.png'/>" id="logoImg">
	</div>
	<section>
	<h1 class="main-title">
		WELCOME TO YOUR<br>HEALTH PARTNER VITABUDDY
		</h1>
        <!-- 로그인 폼 -->
        <div class="loginForm">
            <form id="loginForm">
                <label for="id">아이디</label>
                <br>
                <input type="text" id="id" name="id" class="formTxt" placeholder="아이디" required>
                <br>
                <label for="pwd">비밀번호</label>
                <br>
                <input type="password" id="pwd" name="pwd" class="formTxt" placeholder="비밀번호" autocomplete="off" required>
                <br>
                <button type="submit" id="loginSubmit" class="btn btnFilled">로그인</button>
            </form>

            <!-- 소셜 로그인 버튼 -->
            <div id="socialLogin">
            <!-- 카카오 로그인 -->
                <a href="https://kauth.kakao.com/oauth/authorize?client_id=d04c3a1dba697423aa56a189f1e5f65b&redirect_uri=http://localhost:8080/oauth/kakao/callback&response_type=code&prompt=login">
                    <img src="<c:url value='/image/kakao.png'/>" id="kakaologoImg">
                </a>
            <!-- 네이버 로그인 -->
                <a href="<c:url value='/member/socialLogin/naver'/>">
                    <img src="<c:url value='/image/naver.png'/>" id="logoImg">
                </a>
            <!-- 구글 로그인 -->
                <a href="/member/socialLogin/google">
                    <img src="<c:url value='/image/google.png'/>" alt="네이버 로그인" id="naverLoginBtn">
                </a>
            </div>
        </div>
    </section>

<br>
	<section>
           <!-- 회원가입 -->
        <div>
            <p>아직 회원이 아니신가요?</p>
            <a href="<c:url value='/member/register'/>" class="btn btnFilled">
				회원가입
			</a>
        </div>

        <!-- 로그인 하지 않은 상태로 메인 이동 -->
        <div>
        	<a href="<c:url value='/'/>">
				둘러보기
			</a>
        </div>
	</section>
</div>
</body>
</html>