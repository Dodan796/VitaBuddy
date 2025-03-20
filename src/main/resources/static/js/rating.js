/**
 * 
 */
 
 $(document).ready(function() {
    //별점 클릭
    $('.ratingInput').on('change', function() {
        const selectedRating = $(this).val(); //선택된 별점 값
        
        //모든 별의 스타일을 초기화
        $('.starIcon').each(function() {
            $(this).html('<i class="fa-regular fa-star"></i>'); //빈 별
        });
        
        //선택된 별 이하의 모든 별을 채워진 별로 변경
        $('.starIcon').each(function() {
            const starValue = $(this).data('value');
            if (starValue <= selectedRating) {
                $(this).html('<i class="fa-solid fa-star"></i>'); //채워진 별
            }
        });
    });
});
 