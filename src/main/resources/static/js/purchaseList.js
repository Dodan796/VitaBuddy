/**
 * 마이페이지 구매 내역 영역 - 사용자의 주문내역 확인
 */
    function showPurchases(purchaseType) {
        // 모든 구매 내역을 숨김
        document.getElementById("recentPurchases").style.display = "none";
        document.getElementById("midTermPurchases").style.display = "none";
        document.getElementById("oldPurchases").style.display = "none";

        // 선택한 구매 내역만 표시
        document.getElementById(purchaseType).style.display = "table-row-group";
    }