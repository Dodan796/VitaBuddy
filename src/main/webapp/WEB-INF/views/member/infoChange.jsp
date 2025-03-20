<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
        <script src="<c:url value='/js/infoChange.js'/>"></script>  <!-- infoChange.js 스크립트 삽입 -->
        <script src="<c:url value='/js/memberChangeCheck.js'/>"></script>  <!-- memberChangecheck (이메일, 비번 정합성 검사) 스크립트 삽입 -->
        <script src="<c:url value='/js/searchZip.js'/>"></script> <!-- searchZip.js 우편번호 찾기 스크립트 삽입 -->
        <script src="https://kit.fontawesome.com/567f0760c2.js" crossorigin="anonymous"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <link rel="stylesheet" type="text/css" href="<c:url value='/css/infoChange.css'/>" >   <!-- 로컬 sts 환경에 맞춰 경로 지정 -->
        <c:import url="/WEB-INF/views/layout/head.jsp"/> 
        <meta charset="UTF-8"> 
        <title>회원 정보/ 영양제 관리</title>
    </head>

    <body>
        <c:import url="/WEB-INF/views/layout/top.jsp"/> 
        <section id="wrap"> 
            <div class="container">
                <!-- <div class="headers"> 
                    <h1>회원 정보/ 영양제 관리</h1>
                </div>  -->

				<div class="temp">
					<div class="left">
					
					<div class="headers"> 
                    	<h1>회원 정보 관리</h1>
                	</div> 
						<form method="post" id="information" action="<c:url value='/member/myInfoUpdate'/>" >  
							<div class="horizontal_box">
								<label>이름</label>
								<input type="text" name="userName" value="${myInfo.userName}" required/>
								<p></p>
								<label>비밀번호 변경</label>
			            		<input type="password"  name="userPwd" id="userPwd" oninput="pwCheck()" />  <!-- required 제거 -->
			            		<p id="pwLeng"></p>
			            		<label>비밀번호 확인</label>
								<input type="password"   name="confirmPwd" id="confirmPwd" oninput="pwCheck()" />   <!-- required 제거 -->
								<p id="pwOk"></p>
								<label>이메일 변경</label>
								<input type="email" name="userEmail" id="userEmail" onchange="emailCheck()"  value="${myInfo.userEmail}" required />
								<p id="emOk"></p>
								<label>전화 번호 변경</label>

								<div class="phone-number">

									<input type="text" name="userPh1" id="userPh1" placeholder="010" maxlength="3" value="${fn:split(myInfo.userPh,'-')[0]}" required/>
									 - 
									<input type="text" name="userPh2" id="userPh2" placeholder="1111" maxlength="4" value="${fn:split(myInfo.userPh,'-')[1]}" required/>
									 - 
									<input type="text" name="userPh3" id="userPh3" placeholder="1234" maxlength="4" value="${fn:split(myInfo.userPh,'-')[2]}" required/>
								</div>
								<p></p>
								<div class="zipcode">
									<label>우편번호 입력</label>
									<div class="box_rowContents">
										<input type="text" name="userZipcode" id="userZipcode" value="${myInfo.userZipcode}" required/>  <!-- value값 추가 : form 페이지에 보일 수 있게 -->
										<button type ="button" name="searchBtn" onclick=searchZip()>우편번호 찾기</button>
									</div>
									<p></p>
									<label>주소 변경</label>
									<input type="text" name="userAddress1" id="userAddress1" value="${myInfo.userAddress1}" required/>
									<p></p>
									<label>상세 주소 변경</label>
									<input type="text" id="userAddress2" name="userAddress2"  value="${myInfo.userAddress2}" required/>
								</div>
							</div>

					</div>
					
					<div class="divider"></div>

							<!-- 여기서부터 영양제 정보 수정 변경 - 팀장님 파트 -->
<!--  temp div, 폼 태그 내부 button 앞으로 이동  --><div class="right">
					<div class="headers"> 
                    	<h1>영양제 정보 관리</h1>
                	</div> 
                        <div class="search-box">
<!--       위치 이동(85)                    <i class="fas fa-search"></i> -->
                            <input type="text" id="keyword" placeholder="Search">
                            <select id="brand">
                                <option value="">브랜드 선택</option>
                                <option value="California Gold Nutrition">California Gold Nutrition</option>
                                <option value="Codeage">Codeage</option>
                                <option value="NOW Foods">NOW Foods</option>
                                <option value="Solgar">Solgar</option>
                                <option value="Swanson">Swanson</option>
                                <option value="Thorne">Thorne</option>
                            </select>
                            <!-- 검색 버튼에 아이콘 이동 <button type="button" onclick="searchSupplements()">검색</button> -->
                            <button type="button" onclick="searchSupplements()"><i class="fas fa-search"></i></button>
                        </div>
                        <!-- 검색 결과가 표시될 영역 -->
                        <div id="supplementList"></div>

                        <!-- 복용 중인 영양제 -->
                        <!-- 새로운 div supList 생성 -->
                        <div class="supList">
                        <label>복용 중인 영양제</label>
	                        <ul id="currentSupplementList">
	                            <c:forEach var="supplement" items="${userSupplements}">
	                                <li>${supplement.supName} - ${supplement.supBrand}
                                	<button onclick="deleteSupplement(${supplement.supID})">삭제</button><!-- 삭제버튼 추가. --></li>
	                            </c:forEach>
	                        </ul>
                        </div>
                    </div>
                    </div>
                    <!-- br 태그 추가 -->
                    <br>
                    <!-- 버튼 컨테이너 생성 -->
                    <div class="button-container">
						<button class="submit-btn" id="submitBtn" type="submit" >회원 정보/ 영양제 변경</button>  <!--정보변경 버튼, 즉 submit을 눌렀을 때, 위 form 태그 시작쪽의 onsubmit이 적용되고 requestmapping url로 이동할 수 있도록 type="submit" 추가-->
					</div>
						</form>                             
            </div>
        </section>
        <c:import url="/WEB-INF/views/layout/footer.jsp"/>
    </body>
</html>