<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>상품 등록</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/supplementRegister.css'/>">
    <c:import url="/WEB-INF/views/layout/adminHead.jsp"/>
</head>
<body>
<c:import url="/WEB-INF/views/layout/adminSideBar.jsp"/>
<div class="main">
    <div class="container">
        <h1>상품 등록</h1>
        <form method="post" enctype="multipart/form-data" action="<c:url value='/insertSupplement/admin'/>">
            <div class="form-group">
                <label for="supId">상품 ID:</label>
                <!-- 자동 생성된 ID 표시 -->
                <input type="text" id="supId" name="supId" value="${nextId}" readonly>
            </div>
            <div class="form-group">
                <label for="supName">상품명:</label>
                <input type="text" id="supName" name="supName" required>
            </div>
            <div class="form-group">
                <label for="supPrice">가격:</label>
                <input type="number" id="supPrice" name="supPrice" required>
            </div>
            <div class="form-group">
                <label for="supBrand">브랜드:</label>
                <select id="supBrand" name="supBrand" required>
                    <c:forEach var="brand" items="${brands}">
                        <option value="${brand}">${brand}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="supDosage">용량:</label>
                <input type="text" id="supDosage" name="supDosage" required>
            </div>
            <div class="form-group">
                <label for="supNutri">영양 성분:</label>
                <input type="text" id="supNutri" name="supNutri" required>
            </div>
            <div class="form-group">
                <label for="supNutriinfo">영양 성분 정보:</label>
                <textarea id="supNutriinfo" name="supNutriinfo" required></textarea>
            </div>
            <div class="form-group">
                <label for="supPrecautions">주의 사항:</label>
                <textarea id="supPrecautions" name="supPrecautions" required></textarea>
            </div>
            <div class="form-group">
                <label for="supDetail">상품 상세:</label>
                <textarea id="supDetail" name="supDetail" required></textarea>
            </div>
            <div class="form-group">
                <label for="supImg">이미지 업로드:</label>
                <input type="file" id="supImg" name="supImgFile" accept="image/*">
            </div>
            <button type="submit">등록</button>
        </form>
    </div>
    </div>
</body>
</html>
