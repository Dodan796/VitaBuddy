/**
 *
 */
$(document).ready(function () {
	//테스트 수정 - 별점 초기화하는 코드
	if (window.location.pathname.includes("/edit")) {  // URL에 "/edit"이 포함되어 있는지 확인 후, 
	    $("input[name='rating']").prop("checked", false);
	  }

  $("#reviewForm").on("submit", function () {
    event.preventDefault(); // 기본 제출 방지
    
    //1. 별점 선택
    const selectedRating = $("input[name='rating']:checked").length
    //console.log(selectedRating);  테스트 출력 : 별점을 선택하지 않고 "수정완료" 버튼을 누르면 길이가 0이라고 출력된다
    if (selectedRating==0) {   //별점 length 가 0일 때
      alert("별점을 입력해주세요.");
      return;
    }
    
	//2. 해시태그
    //if (!$("#hashtag").val()) {
      //alert("해시태그를 선택해 주세요.");
      //return;
    //}

	//3. 복용기간
    const startDate = $("#date1").val();
    const endDate = $("#date2").val();
    if (!startDate || !endDate) {
      alert("복용기간을 입력해주세요.");
      return;
    }
    
    //끝 ----------------

    // 리뷰 수정 확인 알림
    if (!confirm("리뷰를 수정하시겠습니까?")) {
      return; // '취소'를 누르면 폼 제출 취소
    }

    const form = this; // 현재 폼

    $.ajax({
      type: "POST",
      url: $(form).attr("action"),
      data: new FormData(form),
      processData: false,
      contentType: false,
      success: function () {
        alert("리뷰가 수정되었습니다.");

        // 부모 창 새로고침
        if (window.opener && !window.opener.closed) {
          window.opener.location.reload();
        }

        // 팝업 창 닫기
        window.close();
      },
      error: function (error) {
        console.error("폼 제출 오류: ", error);
        alert("수정 중 오류가 발생했습니다.");
      },
    });
  });
});