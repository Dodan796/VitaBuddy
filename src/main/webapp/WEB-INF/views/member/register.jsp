<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script src="/js/jquery-3.7.1.min.js"></script>  <!--라이브러리 추가 -->
    <script src="https://kit.fontawesome.com/567f0760c2.js" crossorigin="anonymous"></script>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <title>회원가입</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/register.css'/>">
    <script src="<c:url value='/js/emailVerification.js'/>"></script> <!-- 이메일 인증 코드 추가 -->
    <script src="<c:url value='/js/register.js'/>"></script> <!-- JS 파일을 추가 -->

    <c:import url="/WEB-INF/views/layout/head.jsp"/>
</head>
<body>
    <c:import url="/WEB-INF/views/layout/top.jsp"/>

    <section id="wrap"> <!-- div 에서 section으로 변경 -->
        <div class="container">
            <div class="headers"><!-- 헤더 따로 div 제공 -->
            <h1>회원 가입</h1>
            </div>
            <form onsubmit="return false;">
             <div class="horizontal_box">
            <!-- 구조변경. right left로 나뉜 div horizontal_box로 하나로 통합 -->
                <div class="box_rowContents"> <!-- 각 항목 마다 div로 이름/입력창 구조로 되게끔 반영 -->
                    <label>이름</label>
                    <input type="text" name="userName" id="userName" required/>
                </div>
                <p></p> <!-- 간격 만들기 -->
                <div class="box_rowContents">
                    <label>아이디</label>
                    <input type="text" name="userId" id="userId" required/>
                    <!-- 버튼에 스타일 적용, 목록 옆에 띄움 -->
                    <button type="button" onclick="fnDbCheckId()" name="dbCheckId" class="btn" style="position:absolute; right: 28%; width: 7%;">중복확인</button>
                </div>
                <p></p> <!-- 간격 만들기 -->
                <div class="box_rowContents">
                    <label>비밀번호 설정</label>
                    <input type="password" name="userPwd" id="userPwd" oninput="pwCheck()" required/>
                </div>
                <p id="pwLeng"></p> <!-- 줄 바꿈을 br에서 p로 통일함 -->
                <div class="box_rowContents">
                    <label>비밀번호 확인</label>
                    <input type="password" name="confirmPwd" id="confirmPwd" oninput="pwCheck()" required/><br>
                </div>
                <p id="pwOk"></p>

                <div class="box_rowContents">
                        <label>전화번호</label>
                        <div class="phone-number">
                        <input type="tel" name="userPh1" id="userPh1" placeholder="010" maxlength="3" required/>
                        <span>-</span>
                        <input type="tel" name="userPh2" id="userPh2" placeholder="1111" maxlength="4" required/>
                        <span>-</span>
                        <input type="tel" name="userPh3" id="userPh3" placeholder="1234" maxlength="4" required/>
                    </div>
                </div>
                <p></p>
                <div class="box_rowContents">
                    <label>이메일</label>
                    <input type="email" name="userEmail" id="userEmail" onchange="emailCheckfromDB()" required/>
                    <button type="button" id="verificationEmail">이메일 인증</button><br>
                </div>
                <p id="emOk"></p>
                <div class="box_rowContents" id="verificationEmailCode">
                     <!-- 25/01/05 추가 코드 : ajax 통신으로 동적으로 들어갈 코드 -->
                </div>
                <br>
                <!-- </div>
                <div class="right"> 통합 위해 삭제-->
                <div class="zipcode">
                    <div class="box_rowContents">
                        <label>우편번호</label>
                        <input type="text" name="userZipcode" id="userZipcode" readonly/>
                        <!-- 버튼에 스타일 적용, 목록 옆에 띄움 -->
                        <button type="button" name="searchBtn" class="btn" onclick="openZipSearch()" style="position:absolute; right: 28%; width: 7%;">우편번호 찾기</button><br>             
                    </div>
                    <p></p>
                    <div class="box_rowContents">
                        <label>주소</label>
                        <input type="text" name="userAddress1" id="userAddress1" readonly/>
                    </div>
                    <p></p>
                    <div class="box_rowContents">
                        <label>상세 주소</label>
                        <input type="text" name="userAddress2" id="userAddress2" required/>
                    </div>
                </div>
            </div>
        </form>
        <button class="submit-btn" type="button" id="submitBtn" onclick="registerMember()">회원가입</button>
        </div>
    </section>
<c:import url="/WEB-INF/views/layout/footer.jsp"/>

</body>
</html>