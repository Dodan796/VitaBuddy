<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="<c:url value='/css/adminSideBar.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
            $(document).ready(function(){
                // 상위 메뉴(.toggle) 클릭 시 다음 ul(하위 메뉴)을 토글
                $('.toggle').click(function(e){
                    e.preventDefault(); // a 태그의 기본 링크 동작 막기
                    $(this).next('ul').slideToggle();
                });
            });
        </script>