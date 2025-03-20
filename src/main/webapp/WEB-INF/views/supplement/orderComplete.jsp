<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>   
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>주문 완료</title>
		<link rel="stylesheet" type="text/css" href="<c:url value='/css/orderComplete.css'/>">
 		<c:import url="/WEB-INF/views/layout/head.jsp" />	 	
	</head>
	<body>
		<div id="wrap">
        	<!--  top -->         
        	<c:import url="/WEB-INF/views/layout/top.jsp" />
			<section class="ordComplete">		
				<h3>주문이 완료되었습니다</h3>
				<p>주문내역은 마이페이지에서 확인할 수 있습니다.</p>
				
				 <div class="button-container">
			        <a href="<c:url value='/member/myPage'/>" class="btn btnFilled">마이페이지</a>
			        <a href="<c:url value='/supplement/supplementList'/>" class="btn btnUnfilled">상점</a>
			    </div>
			</section> 
		
			<!--  footer -->         
 	        	<c:import url="/WEB-INF/views/layout/footer.jsp" /> 
      </div>
	</body>
</html>