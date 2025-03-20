$(document).ready(function() {
    $('.plusCartBtn').on('click', function() {
        const supId = $(this).data('sup-id');
        const userId = $(this).data('user-id');

        // 로그인 상태 실시간 체크
        $.ajax({
            type: 'GET',
            url: '/api/checkAuth',
            success: function() {
                // 인증된 상태
                $.ajax({
                    type: 'POST',
                    url: '/supplement/wishList/toCart',
                    data: JSON.stringify({
                        supId: supId,
                        userId: userId
                    }),
                    contentType: 'application/json',
                    success: function(response) {
                        if (response === 1) {
                            alert('장바구니에 추가되었습니다');
                        } else {
                            alert('장바구니 추가 실패');
                        }
                    },
                    error: function() {
                        alert('장바구니 추가 실패');
                    }
                });
            },
            error: function(xhr) {
                if (xhr.status === 401) {
                    alert('로그인이 필요한 서비스입니다.');
                    window.location.href = "/intro";
                }
            }
        });
    });
});
