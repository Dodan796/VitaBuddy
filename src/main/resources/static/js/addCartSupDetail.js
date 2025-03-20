$(document).ready(function() {
    $('#addCart').on('click', function(event) {
        event.preventDefault(); // 버튼의 기본 동작 방지

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
                    url: '/api/addCart',
                    data: JSON.stringify({
                        supId: supId,
                        userId: userId
                    }),
                    contentType: 'application/json',
                    success: function(response) {
                        if (response == 1) {
                            alert('장바구니에 추가되었습니다');
                            history.replaceState(null, null, window.location.href);
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

    // 뒤로 가기 시 새로고침
    $(window).on("pageshow", function(event) {
        if (event.originalEvent.persisted) {
            window.location.reload();
        }
    });
});
