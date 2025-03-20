$(document).ready(function () {
    $.ajax({
        url: `/supplement/supplementDetail/${supId}/reviews`,  // JSP에서 가져온 supId 사용
        type: 'GET',
        success: function (data) {
            console.log(data); // JSON 형태의 리뷰 데이터를 확인
            // 리뷰 데이터를 HTML에 추가하는 코드
            data.forEach(function(review) {
                $('#reviewList').append(`
                    <div class="review-item">
                        <h3>${review.reviewTitle}</h3>
                        <p>작성자: ${review.userId}</p>
                        <p>내용: ${review.content}</p>
                        <!-- 다른 필요한 정보를 추가로 HTML로 표시 -->
                    </div>
                `);
            });
        },
        error: function (xhr, status, error) {
            alert('리뷰 목록을 불러오는 중 오류가 발생했습니다.');
        }
    });
});
