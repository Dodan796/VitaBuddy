/**
 * 
 */
$(document).ready(function() {
    $('.deleteCartBtn').on('click', function() {
        // 삭제 확인 메시지 추가
        if (!confirm('장바구니에서 삭제하시겠습니까?')) {
            return; 
        }

        const cartId = $(this).data('cart-id');  // cartId 추가
        
		console.log(cartId);
        $.ajax({
            type: 'post',
            url: '/api/deleteCart',
            data: { cartId: cartId},   // cartId 추가
            //contentType: 'application/json',  //데이터 형식이 json 형식임을 명시
            success: function(response) {
            	if(response == 1 ){
                alert('삭제되었습니다');
                location.href="/supplement/cartList";   //삭제 완료 이후, 장바구니 목록 재요청
                }
            },
            error: function() {
                alert('삭제 실패'); 
            }
        });
    });
});
