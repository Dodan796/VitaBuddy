<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 상세 조회</title>
<script src="<c:url value='/js/jquery-3.7.1.min.js'/>"></script>
<script src="<c:url value='/js/rating.js'/>"></script>
<script src="<c:url value='/js/insertReview.js'/>"></script>
<script src="<c:url value='/js/deleteReview.js'/>"></script>
<script src="<c:url value='/js/editReviewForm.js'/>"></script>
<script src="<c:url value='/js/supplementDetail.js'/>"></script>
<script src="<c:url value='/js/reviewPagenation.js'/>"></script>
<!-- 찜목록 추가, 장바구니 추가 -->
<script src="<c:url value='/js/addWish.js'/>"></script>
<script src="<c:url value='/js/addCartSupDetail.js'/>"></script>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/css/supplementDetail.css'/>">
<c:import url="/WEB-INF/views/layout/head.jsp" />

<!-- 1110 테스트 출력 : jsp 에서 세션아이디값을 지정해놓고 테스트 출력  -->
<script>
    const userId = "${sessionScope.sid}"; // 서버 세션에서 가져온 userId를 JavaScript 변수에 저장
    console.log("JSP User ID from session:", userId); // 콘솔에 userId 출력
    console.log("JSP User ID from session:", review.userId);
</script>

</head>
<body>
	<div id="supplementDetail"
		data-supplement-id="${supplementDetail.supId}"></div>
	<div id="wrap">
		<!-- top 메뉴 포함 -->
		<c:import url="/WEB-INF/views/layout/top.jsp" />

		<!-- 로그인 여부 데이터 속성으로 설정 -->
		<c:set var="isLoggedIn" value="${not empty sessionScope.sid}" />
		<!-- 로그인 여부 저장 -->
		<div id="loginStatus" data-login="${isLoggedIn}"></div>

		<!-- 상품 프로필 -->
		<section class="prdProfile">
			<!-- 공백 삭제 <br><br> -->
			<table>
				<tr>
					<td colspan="4">
						<h2>${supplementDetail.supName}</h2>
					</td>
					<td rowspan="6" colspan="2">
						<!-- 이미지 표시 방식 수정: Base64 인코딩으로 변환된 이미지를 표시 --> <img
						class="prdImg" src="data:image/png;base64,${supImgBase64}"
						width="300" height="300" alt="Product Image">
					</td>
				</tr>

				<%-- <!-- 별점 -->
					<tr>
						<td>
					              <div class="rating">
	         <c:forEach var="i" begin="1" end="5">
	               <i class="${i <= sup.rating ? 'fa-solid fa-star' : 'fa-regular fa-star'}"></i>
	           </c:forEach>
	           </div>
	               </td>
	               <td></td>
	               <td></td>
	               <td></td>
	            </tr> --%>

				<!-- 찜목록 및 장바구니 추가 -->
				<tr>

					<!-- 찜목록, 장바구니 추가 버튼에 data 속성 추가 -->
					<td colspan="2"><a href="#" id="addWish"
						data-sup-id="${supplementDetail.supId}"
						data-user-id="${sessionScope.sid}">찜목록 추가</a></td>
					<td colspan="2"><a href="#" id="addCart"
						data-sup-id="${supplementDetail.supId}"
						data-user-id="${sessionScope.sid}">장바구니 추가</a> <!-- 10/28 수정사항 href # 재추가. -->
					</td>
					<!-- <td></td> -->
				</tr>

				<!-- 가격 정보 -->
				<tr>
					<td>가격</td>
					<td colspan="2"><span id="price"
						data-price="${supplementDetail.supPrice}"> <fmt:formatNumber
								value="${supplementDetail.supPrice}" pattern="#,###" />
					</span> 원</td>
					<td></td>
				</tr>

				<!-- 브랜드 정보 -->
				<tr>
					<td>브랜드</td>
					<td colspan="2">${supplementDetail.supBrand}</td>
					<td></td>
				</tr>
				<!-- 해시태그 -->
				<!-- 상위 2개 해시태그 -->
				<tr>
					<td>해시태그</td>
					<td colspan="2">
						<div class="hashTag">
							<c:choose>
								<c:when test="${not empty topHashtags}">
									<c:forEach var="hashtag" items="${topHashtags}">
				                        ${hashtag.reviewHashtag}&nbsp;&nbsp;
				                    </c:forEach>
								</c:when>
								<c:otherwise>
									<span>해시태그가 없습니다</span>
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
			</table>
		</section>

		<br>

		<!-- 상품 상세 정보 -->
		<section class="prdInfo">
			<h2>상품 상세 정보</h2>
			<div>
				<!-- 상품 설명 -->
				<div>
					<h3>상품설명</h3>
					<p>${supplementDetail.supDetail}</p>
				</div>

				<!-- 복용법 -->
				<div>
					<h3>복용법</h3>
					<p>${supplementDetail.supDosage}</p>
				</div>

				<!-- 주의사항 -->
				<div>
					<h3>주의사항</h3>
					<p>${supplementDetail.supPrecautions}</p>
				</div>

				<!-- 영양 정보 -->
				<div>
					<h3 id="toggleNutrition" class="clickable">
						영양정보 <span class="arrow"><i class="fa-solid fa-caret-down"></i></span>
					</h3>
					<div id="nutritionInfo" class="hidden">
						<table>
							<tr>
								<td class="supNutri">${supplementDetail.supNutri}</td>
								<td>${supplementDetail.supNutriInfo}</td>
							</tr>
						</table>
					</div>
				</div>
			</div>

		</section>

		<!-- 리뷰작성 -->
		<!-- div를 section으로 변경 1018 -->
		<section class="reviewWrite">
			<form method="post" id="reviewForm" enctype="multipart/form-data"
				action="/supplement/supplementDetail/${supplementDetail.supId}/review">
				<!-- sup.supId -> supplement.supId -->
				<input type="hidden" name="reviewNo" value="${review.reviewNo}">
				<input type="hidden" name="userId" value="${sessionScope.sid}">
				<input type="hidden" name="supId" value="${supplementDetail.supId}">
				<!-- 리뷰 제목 -->
				<label>리뷰제목</label> <br> <input class="reviewTitle" type="text"
					name="reviewTitle" value="${review.reviewTitle}"> <br>

				<!-- 별점 -->
				<div class="rating">
					<label class="ratingLabel" for="star1"> <input type="radio"
						id="star1" class="ratingInput" name="rating" value="1"
						${review.rating == 1 ? 'checked' : ''}> <span
						class="starIcon" data-value="1"><i
							class="fa-regular fa-star"></i></span>
					</label> <label class="ratingLabel" for="star2"> <input
						type="radio" id="star2" class="ratingInput" name="rating"
						value="2" ${review.rating == 2 ? 'checked' : ''}> <span
						class="starIcon" data-value="2"><i
							class="fa-regular fa-star"></i></span>
					</label> <label class="ratingLabel" for="star3"> <input
						type="radio" id="star3" class="ratingInput" name="rating"
						value="3" ${review.rating == 3 ? 'checked' : ''}> <span
						class="starIcon" data-value="3"><i
							class="fa-regular fa-star"></i></span>
					</label> <label class="ratingLabel" for="star4"> <input
						type="radio" id="star4" class="ratingInput" name="rating"
						value="4" ${review.rating == 4 ? 'checked' : ''}> <span
						class="starIcon" data-value="4"><i
							class="fa-regular fa-star"></i></span>
					</label> <label class="ratingLabel" for="star5"> <input
						type="radio" id="star5" class="ratingInput" name="rating"
						value="5" ${review.rating == 5 ? 'checked' : ''}> <span
						class="starIcon" data-value="5"><i
							class="fa-regular fa-star"></i></span>
					</label>
				</div>

				<br>

				<table class="option">
					<tr>
						<td></td>
						<td></td>
						<td><label for="date">복용기간</label></td>
					</tr>

					<!-- 해시태그 -->
					<tr>
						<td><select id="hashtag" name="reviewHashtag">
								<option value="">없음</option>
								<option value="#가성비최고">#가성비최고</option>
								<option value="#효과빠름">#효과빠름</option>
								<option value="#복용간편">#복용간편</option>
								<option value="#지속적인 효과">#지속적인 효과</option>
								<option value="#어린이도 잘먹어요">#어린이도 잘먹어요</option>
								<option value="#피로회복에 좋아요">#피로회복에 좋아요</option>
								<option value="#소화가 편해요">#소화가 편해요</option>
								<option value="#근육회복에 좋아요">#근육회복에 좋아요</option>
								<option value="#탈모예방에 좋아요">#탈모예방에 좋아요</option>
								<option value="#눈이 편안해요">#눈이 편안해요</option>
								<c:forEach var="tag" items="${taglist}">
									<option value="${tag.tagNo}">${tag.tagName}</option>
								</c:forEach>
						</select></td>

						<!-- 복용기간 설정 -->
						<td><input type="date" id="date1" name="startDate"
							value="${review.startDate}" max="2100-12-31" min="2000-01-01">
							~</td>
						<td><input type="date" id="date2" name="endDate"
							value="${review.endDate}" max="2100-12-31" min="2000-01-01">
						</td>
					</tr>
				</table>

				<!-- 리뷰 내용 -->
				<br>
				<textarea class="reviewTxt" name="content" cols="150" rows="5">${review.content}</textarea>
				<br>
				<!-- 사진 첨부 -->
				<input class="Upload" type="file" id="uploadFile" name="reviewImg"
					multiple> <br>

				<!-- submit -->
				<input type="submit" value="작성하기" class="btn btnFilled">
			</form>
		</section>
		<!-- div를 section으로 변경 1018 -->

		<!-- 리뷰목록 -->
		<!-- div를 section으로 변경 1018 -->
		<section class="reviews">
			<div id="reviewListContainer">
				<c:forEach var="review" items="${reviewList}">
					<table class="reviewItem">
						<tr>
							<td colspan="5"><h3>${review.reviewTitle}</h3></td>
							<!-- 리뷰 수정 -->
							<%-- <td>
							<c:if test="${(not empty sessionScope.sid and review.userId eq sessionScope.sid) or (not empty myUserId and review.userId eq myUserId)}">
							    <a href="#" class="correctReview" data-review-id="${review.userId}">수정</a>
							</c:if>
							<c:if test="${(not empty sessionScope.sid and review.userId eq sessionScope.sid) or (not empty myUserId and review.userId eq myUserId)}">
                                <a href="#" class="correctReview" data-review-id="${review.userId}">수정</a>
                            </c:if>
						</td> --%>
							<!-- 리뷰 삭제 -->
							<%-- <td>
					    		<c:if test="${(not empty sessionScope.sid and review.userId eq sessionScope.sid) or (not empty myUserId and review.userId eq myUserId)}">
									<a href="#" class="deleteReview" data-review-id="${review.userId}">리뷰삭제</a>
					    		</c:if>
					    		<c:if test="${(not empty sessionScope.sid and review.userId eq sessionScope.sid) or (not empty myUserId and review.userId eq myUserId)}">
                                    <a href="#" class="deleteReview" data-review-id="${review.userId}">리뷰삭제</a>
                                </c:if>
							</td> --%>
							<!-- 리뷰삭제 수정된 부분 -->
							<!-- 삭제 버튼 style="display:inline;" 제거 class="deleteReview" 추가 -->
							<td colspan="2">
								<!-- 수정 삭제 버튼 한 칸에/ 순서 변경-->
								<c:if test="${(not empty sessionScope.sid and review.userId eq sessionScope.sid) or (not empty myUserId and review.userId eq myUserId)}">
									<button class="deleteReview"
										data-review-id="${review.reviewNo}"
										data-sup-id="${supplementDetail.supId}">삭제</button>
								</c:if>
								<c:if test="${(not empty sessionScope.sid and review.userId eq sessionScope.sid) or (not empty myUserId and review.userId eq myUserId)}">
									<button class="editButton"
										data-sup-id="${supplementDetail.supId}"
										data-review-no="${review.reviewNo}">수정</button>
								</c:if>
							</td>
						</tr>

						<!-- 작성자 정보 -->
						<tr>
							<td colspan="4" class="userInfo">
								<p>작성자: ${review.userId}</p> <br>
								<p>
									작성일:
									<fmt:formatDate value="${review.reviewDate}"
										pattern="yyyy-MM-dd" />
								</p>
							</td>
							<!-- 이미지 파일 출력 -->
							<!-- 이미지 칸 합침, 반복문 밖으로 td이동/이미지 태그의 width="80" height="80" 값 삭제-->
							<td class="reviewImg" colspan="3"><c:forEach var="img"
									items="${fn:split(review.reviewImg, ';')}">
									<!-- <td class="reviewImg"> -->
									<img class="reviewImg" src="/Review_Upload/${img}">
									<!-- </td> -->
								</c:forEach></td>
						</tr>
						<!-- 리뷰 내용 -->
						<tr>
							<td colspan="7" class="content" height="40">
								<p>${review.content}</p>
							</td>
						</tr>
						<!-- 기간 및 해시태그 -->
						<tr class="supInfo">
							<!-- 테이블 구조 수정 colspan 3 >2 -->
							<td></td>
							<!-- 복용기간 칸 합침 -->
							<td colspan="3">복용기간: <fmt:formatDate
									value="${review.startDate}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate
									value="${review.endDate}" pattern="yyyy-MM-dd" /></td>
							<td  colspan="2">해시태그: ${review.reviewHashtag}</td>
							<td>
								<div class="rate">
									<c:forEach var="i" begin="1" end="5">
										<i
											class="${i <= review.rating ? 'fa-solid fa-star' : 'fa-regular fa-star'}"></i>
									</c:forEach>
								</div>
							</td>
						</tr>

						<!-- 신고하기 버튼 -->
						<tr>
							<td colspan="6"></td>
							<td><a href="#" class="btn btnUnfilled">신고하기</a></td>
						</tr>
					</table>
				</c:forEach>
			</div>
		</section>
		<!-- div를 section으로 변경 1018 -->

		<nav>
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
				<button
					class="prev <c:if test='${currentPage == 1}'>disabled</c:if>"
					data-page="${currentPage - 1}"
					onClick="goToPage(${currentPage - 1})">
					<i class="fa-solid fa-caret-left"></i>
				</button>

				<!-- 페이지 번호 버튼 -->
				<c:forEach var="i" begin="${startPage}" end="${endPage}">
					<button
						class="page <c:if test='${i == currentPage}'> active</c:if>"
						onClick="goToPage(${i})" data-page="${i}">${i}</button>
				</c:forEach>

				<!-- 다음 버튼 -->
				<button
					class="next <c:if test='${currentPage == totalPages}'>disabled</c:if>"
					data-page="${currentPage + 1}"
					onClick="goToPage(${currentPage + 1})">
					<i class="fa-solid fa-caret-right"></i>
				</button>
			</div>
		</nav>

		<!-- 		</div> -->

		<!-- footer 포함 -->
		<c:import url="/WEB-INF/views/layout/footer.jsp" />
	</div>
</body>