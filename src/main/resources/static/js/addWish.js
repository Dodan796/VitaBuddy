$(document).ready(function () {
  $("#addWish").on("click", function (event) {
    event.preventDefault();
    const supId = $(this).data("sup-id");

    // 실시간 인증 체크
    $.ajax({
      type: "GET",
      url: "/api/checkAuth",
      success: function () {
        // 인증된 상태에서 찜 추가 요청
        $.ajax({
          type: "POST",
          url: "/supplement/wishList/insert",
          data: JSON.stringify({
            supId: supId
          }),
          contentType: "application/json",
          success: function (response) {
            alert("찜목록에 추가되었습니다.");
          },
          error: function () {
            alert("찜목록 추가 실패");
          }
        });
      },
      error: function (xhr) {
        if (xhr.status === 401) {
          alert("로그인이 필요한 서비스입니다.");
          window.location.href = "/intro";
        }
      }
    });
  });
});
