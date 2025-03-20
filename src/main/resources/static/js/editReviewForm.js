/**
 *
 */

// 부모 페이지에서 수정 버튼 클릭 시 팝업 창 열기
$(document).ready(function () {
  $("#reviewListContainer").on("click", ".editButton", function (event) {  //1110 동적으로 생성된 리뷰 리스트 수정
    event.preventDefault(); // 기본 동작 방지

    const supId = $(this).data("sup-id");
    const reviewNo = $(this).data("review-no");

    // 브라우저 창 크기
    var screenWidth = window.innerWidth;
    var screenHeight = window.innerHeight;

    // %로 창 크기와 위치 계산
    var popupWidth = screenWidth * 0.7; // 너비의 70%
    var popupHeight = screenHeight * 0.65; // 높이의 65%
    // 창 가운데 정렬
    var popupLeft = (screenWidth - popupWidth) / 2;
    var popupTop = (screenHeight - popupHeight) / 2;

    // 경로 설정하여 팝업 창 열기
    const popupUrl = `/api/supplement/supplementDetail/${supId}/review/${reviewNo}/editForm`;

    const popup = window.open(
      popupUrl,
      "editReviewPopup",
      `width=${popupWidth},height=${popupHeight},left=${popupLeft},top=${popupTop}`
    );

    // 팝업이 정상적으로 열렸는지 확인
    if (!popup || popup.closed || typeof popup.closed === "undefined") {
      alert("팝업 차단이 감지되었습니다. 브라우저 설정을 확인하세요.");
    }
  });
});