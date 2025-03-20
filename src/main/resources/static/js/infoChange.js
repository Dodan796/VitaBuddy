$(document).ready(function () {
  // 기존 searchBtn 클릭 이벤트 제거 (JSP에서 직접 호출할 것이므로)
});

// 1. 영양제 검색 함수 (JSP에서 직접 호출)
function searchSupplements() {
  const keyword = document.getElementById("keyword").value;
  const brand = document.getElementById("brand").value;

  $.ajax({
    url: "/supplement/search",
    method: "GET",
    data: { keyword: keyword, brand: brand },
    success: function (data) {
      displaySupplementList(data);
    },
    error: function () {
      alert("영양제 검색에 실패했습니다.");
    },
  });
}

// 2. 검색 결과 표시
function displaySupplementList(supplements) {
  const supplementList = document.getElementById("supplementList");
  supplementList.innerHTML = "";

  supplements.forEach((supplement) => {
    const listItem = document.createElement("li");
    listItem.textContent = `${supplement.supName} (${supplement.supBrand})`;

    const selectButton = document.createElement("button");
    selectButton.textContent = "추가";
    selectButton.onclick = function () {
      checkAuthAndAdd(supplement.supID, supplement);
    };

    listItem.appendChild(selectButton);
    supplementList.appendChild(listItem);
  });
}

// 3. 인증 체크 후 추가
function checkAuthAndAdd(supID, supplement) {
  $.ajax({
    type: "GET",
    url: "/api/checkAuth",
    success: function () {
      addToCurrentList(supplement);
    },
    error: function (xhr) {
      if (xhr.status === 401) {
        alert("로그인이 필요한 서비스입니다.");
        window.location.href = "/intro";
      }
    },
  });
}

// 4. 복용 리스트에 추가 (쿼리 파라미터로 supID 전달)
function addToCurrentList(supplement) {
  const currentList = document.getElementById("currentSupplementList");
  const listItem = document.createElement("li");
  listItem.textContent = `${supplement.supName} (${supplement.supBrand})`;
  listItem.setAttribute("data-id", supplement.supID);

  const deleteButton = document.createElement("button");
  deleteButton.textContent = "삭제";
  deleteButton.onclick = function () {
    deleteSupplement(supplement.supID, listItem);
  };

  listItem.appendChild(deleteButton);
  currentList.appendChild(listItem);

  $.ajax({
    url: `/supplement/add?supId=${supplement.supID}`,
    method: "POST",
    success: function (response) {
      alert(response);
    },
    error: function () {
      alert("영양제 추가에 실패했습니다.");
    },
  });
}

// 5. 삭제 요청 (쿼리 파라미터 방식)
function deleteSupplement(supID, listItem = null) {
  $.ajax({
    url: `/supplement/delete?supId=${supID}`,
    method: "DELETE",
    success: function (response) {
      alert(response);
      if (listItem) {
        listItem.remove();
      } else {
        location.reload();
      }
    },
    error: function () {
      alert("영양제 삭제에 실패했습니다.");
    },
  });
}