$(document).ready(function () {
  // (1) JSP에 세션 로그인 상태를 표시한 값 (true/false)
  var isLoggedIn = $("#loginStatus").data("login");

  // (2) 페이지가 로드되었을 때,
  //     세션 로그인 아닌 상태(isLoggedIn=false)라도, 혹시 JWT일 수도 있으니 체크
  if (!isLoggedIn) {
    $.ajax({
      url: "/api/checkAuth",
      method: "GET",
      xhrFields: { withCredentials: true }, // 쿠키 포함
      success: function () {
        // 서버가 200이면 -> JWT(혹은 세션) 인증 유효
        isLoggedIn = true;
      },
      error: function (xhr) {
        if (xhr.status === 401) {
          // 정말로 비로그인
          // isLoggedIn stays false
        }
      }
    });
  }

  /********************************************
   * (A) 포커스 시 비로그인 경고
   ********************************************/
  $('#reviewForm input:not([type="date"]), #reviewForm textarea').on("focus", function () {
    // 만약 아직도 isLoggedIn이 false라면
    if (!isLoggedIn) {
      $(this).blur();
      alert("리뷰 작성 시 로그인이 필요합니다.");
      // 필요하다면: window.location.href="/intro";
    }
  });

  /********************************************
   * (B) 폼 제출 시 비로그인 경고
   ********************************************/
  $("#reviewForm").on("submit", function (event) {
    if (!isLoggedIn) {
      event.preventDefault();
      alert("리뷰 작성 시 로그인이 필요합니다.");
      return;
    }
  });
});


/********************************************
 * insertReviewAjax.js
 * 실제 AJAX로 리뷰 작성
 ********************************************/
$(document).ready(function () {

  $("#reviewForm").on("submit", function (e) {
    e.preventDefault();

    // (이미 위에서 로그인 여부, 유효성 검사는 처리)
    // 여기서는 서버에 Ajax 전송
    var formData = new FormData(this);
    var supId = $("input[name='supId']").val();

    $.ajax({
      url: `/api/supplement/supplementDetail/${supId}/review`,
      type: "POST",
      data: formData,
      processData: false,
      contentType: false,
      xhrFields: { withCredentials: true },
      success: function () {
        alert("리뷰가 성공적으로 작성되었습니다!");
        window.location.replace(`/api/supplement/supplementDetail/${supId}`);
      },
      error: function (xhr, status, error) {
        console.error("상태 코드:", xhr.status);
        console.error("에러 메시지:", error);
        if (xhr.status === 401) {
          alert("로그인이 필요합니다. 로그인 페이지로 이동합니다.");
          window.location.href = "/intro";
        } else {
          alert("리뷰 작성에 실패했습니다: " + error);
        }
      }
    });
  });

  // 뒤로가기 시 새로고침(캐시 방지)
  $(window).on("pageshow", function (event) {
    if (event.originalEvent.persisted) {
      window.location.reload();
    }
  });
});
