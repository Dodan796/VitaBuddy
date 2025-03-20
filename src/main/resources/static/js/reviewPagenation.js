/*
* pagination 코드
*/

function goToPage(page) {
    const supplementId = $('#supplementDetail').data('supplement-id');
    
    //1110 테스트 코드 : supplementDetail.jsp 의 <head></head> 내부에 <script></script>로 sessionScope.sid 설정 : 세션 아이디값이 js까지 전달된다.
    console.log("User ID from JSP session, 자바스크립트 변수로 출력:", userId); // 세션 userId가 올바르게 전달되는지 확인

    $.ajax({
        url: `/api/supplement/supplementDetail/${supplementId}/reviews`,
        type: 'GET',
        data: { page: page },
        success: function (response) {
            console.log("Ajax Response:", response); // 응답 데이터 확인
            
            const reviewListContainer = $('#reviewListContainer');
            reviewListContainer.empty();

            response.reviewList.forEach(review => {
            
            	// 1110 test 코드 : yurim0000 출력
            	console.log("Review User ID:", review.userId); 
            	
                const reviewHtml = `
                    <table class="reviewItem">
                        <tr> 
                            <td colspan="5"><h3>${review.reviewTitle}</h3></td>
                            <td colspan="2">
                                ${review.userId === userId ? `
                                <button class="deleteReview" data-review-id="${review.reviewNo}" data-sup-id="${supplementId}">삭제</button>
                                <button class="editButton" data-sup-id="${supplementId}" data-review-no="${review.reviewNo}">수정</button>
                                ` : ''}
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" class="userInfo">
                                <p>작성자: ${review.userId}</p><br>
                                <p>작성일: ${new Date(review.reviewDate).toLocaleDateString()}</p>
                            </td>
                            <td class="reviewImg" colspan="3">
                                ${review.reviewImg ? review.reviewImg.split(';').map(img => `<img class="reviewImg" src="/Review_Upload/${img}">`).join('') : ''}
                            </td>
                        </tr>
                        <tr>
                            <td colspan="7" class="content" height="40"><p>${review.content}</p></td>
                        </tr>
                        <tr class="supInfo">
                            <td colspan="2"></td>
                            <td colspan="3">복용기간: ${new Date(review.startDate).toLocaleDateString()} ~ ${new Date(review.endDate).toLocaleDateString()}</td>
                            <td>해시태그: ${review.reviewHashtag}</td>
                            <td>
                                <div class="rate">
                                    ${[...Array(5).keys()].map(i => `<i class="${i < review.rating ? 'fa-solid fa-star' : 'fa-regular fa-star'}"></i>`).join('')}
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="6"></td>
                            <td><a href="#" class="btn btnUnfilled">신고하기</a></td>
                        </tr>
                    </table>
                `;
                reviewListContainer.append(reviewHtml);
            });

            // 페이지 번호 업데이트
            $('.page').removeClass('active');
            $(`.page[data-page=${page}]`).addClass('active');
            
            // 이전/다음 버튼 활성화 또는 비활성화
            $('.prev').toggleClass('disabled', page <= 1).data('page', page - 1);
            $('.next').toggleClass('disabled', page >= response.totalPages).data('page', page + 1);
        },
        error: function (xhr, status, error) {
            console.error("리뷰 목록을 불러오는 중 오류가 발생했습니다:", error);
        }
    });
}

// 페이지 버튼 클릭 시
$(document).on('click', '.page', function () {
    const page = $(this).data('page');
    goToPage(page);
});

// 이전 버튼 클릭 시
$(document).on('click', '.prev:not(.disabled)', function () {
    const page = $(this).data('page');
    goToPage(page);
});

// 다음 버튼 클릭 시
$(document).on('click', '.next:not(.disabled)', function () {
    const page = $(this).data('page');
    goToPage(page);
});