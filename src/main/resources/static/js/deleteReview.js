/**
 * 
 */
$(document).ready(function () {
    $("#reviewListContainer").on("click", ".deleteReview", function () {  //1110 삭제
        let answer = confirm("리뷰를 삭제하시겠습니까?");
        if (answer) {
            const reviewNo = $(this).data('review-id');
            const supId = $(this).data('sup-id');

            $.ajax({
                type: 'POST',
                url: `/api/supplement/supplementDetail/${supId}/review/${reviewNo}/delete`,
                success: function () {
                    alert('리뷰가 삭제되었습니다.');
                    location.reload(); // 페이지 새로고침
                },
                error: function () {
                    alert('리뷰 삭제에 실패했습니다.');
                }
            });
        }
    });
});