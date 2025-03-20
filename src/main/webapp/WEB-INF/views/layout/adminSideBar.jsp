<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="sidebar">
        <img src="<c:url value='/image/logo.png'/>" id="logoImg" width="240" height="80">
        <h2>관리자 메뉴</h2>
        <ul>
            <li>
                <a href="#" class="toggle">상품 관리</a>
                <ul class="supplementAdmin">
                    <li><a href="#">상품 관리</a></li>
                    <li><a href="#">상품 등록</a></li>
                </ul>
            </li>
            <li>
                <a href="#" class="toggle">회원 관리</a>
                <ul class="memberAdmin">
                    <li><a href="#">회원 관리</a></li>
                </ul>
            </li>
            <li>
                <a href="<c:url value='/member/logout'/>">로그아웃</a>
            </li>
        </ul>
    </div>
