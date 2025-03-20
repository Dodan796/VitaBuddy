<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>상품 등록</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/supplementUpdate.css'/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/adminSideBar.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<c:import url="/WEB-INF/views/layout/adminSideBar.jsp"/>
<div class="main">
    <div class="container">
            <h1>상품 정보 업데이트</h1>
            <form action="/updateSupplement" method="post" enctype="multipart/form-data">
                <!-- 이미지-->
                <div class="form-group image-group">
                    <div class="image-preview">
                        <label>현재 이미지</label>
                        <!-- 이미지 표시 방식 수정: Base64 인코딩으로 변환된 이미지를 표시 --> <img
                        						class="prdImg" src="data:image/png;base64,${supImgBase64}"
                        						width="300" height="300" alt="Product Image">
                    </div>
                    <div class="image-update">
                        <label for="supImg">이미지 변경</label>
                        <input type="file" id="supImg" name="supImg" accept="image/*">
                    </div>
                </div>

                <!-- 상품 ID (수정 불가) -->
                <div class="form-group">
                    <label for="supId">상품 ID</label>
                    <input type="text" id="supId" name="supId" value="${supplementDetail.supId}" readonly>
                </div>

                <!-- 상품 이름 -->
                <div class="form-group">
                    <label for="supName">상품 이름</label>
                    <input type="text" id="supName" name="supName" value="${supplementDetail.supName}" required>
                </div>

                <!-- 상품 가격 -->
                <div class="form-group">
                    <label for="supPrice">상품 가격</label>
                    <input type="number" id="supPrice" name="supPrice" value="${supplementDetail.supPrice}" required>
                </div>

                <!-- 브랜드 -->
                <div class="form-group">
                    <label for="supBrand">브랜드</label>
                    <input type="text" id="supBrand" name="supBrand" value="${supplementDetail.supBrand}" required>
                </div>

                <!-- 용량 -->
                <div class="form-group">
                    <label for="supDosage">용량</label>
                    <input type="text" id="supDosage" name="supDosage" value="${supplementDetail.supDosage}" required>
                </div>

                <!-- 영양 성분 -->
                <div class="form-group">
                    <label for="supNutri">영양 성분</label>
                    <textarea id="supNutri" name="supNutri" required>${supplementDetail.supNutri}</textarea>
                </div>

                <!-- 주의사항 -->
                <div class="form-group">
                    <label for="supPrecautions">주의사항</label>
                    <textarea id="supPrecautions" name="supPrecautions" required>${supplementDetail.supPrecautions}</textarea>
                </div>

                <!-- 상세 설명 -->
                <div class="form-group">
                    <label for="supDetail">상세 설명</label>
                    <textarea id="supDetail" name="supDetail" required>${supplementDetail.supDetail}</textarea>
                </div>



                <button type="submit" class="btn-submit">업데이트</button>
            </form>
    </div>
</div>
</body>
</html>
