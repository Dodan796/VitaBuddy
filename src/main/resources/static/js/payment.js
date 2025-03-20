document.addEventListener("DOMContentLoaded", function () {
    const clientKey = "test_ck_5OWRapdA8dwkboG6WZRAro1zEqZK"; // 테스트 클라이언트 키
    const tossPayments = TossPayments(clientKey); // TossPayments 인스턴스 생성

    console.log("TossPayments 객체 생성 확인:", tossPayments); // 객체 출력

    // 비회원 결제 (익명 결제도 지원)
    const payment = tossPayments.payment({ customerKey: TossPayments.ANONYMOUS });

    // 결제 함수 정의
    async function requestPayment() {
        console.log("결제 요청 함수 호출됨");

        const orderId = document.getElementById("orderId").value;
        const amount = parseInt(document.getElementById("amount").value);
        const customerEmail = document.getElementById("ordRcvEmail").value;
        const customerName = document.getElementById("ordRcvReceiver").value;
        let customerMobilePhone = document.getElementById("ordRcvPhone").value;

        // 전화번호 포맷
        customerMobilePhone = customerMobilePhone.replace(/-/g, "");
        // if (!customerMobilePhone.startsWith("82")) {
        //    customerMobilePhone = "82" + customerMobilePhone.slice(1);
        // }

        console.log("결제 정보 확인:", { orderId, amount, customerEmail, customerName, customerMobilePhone });

        try {
            await payment.requestPayment({
                method: "CARD",
                amount: {
                    currency: "KRW",
                    value: amount,
                },
                orderId: orderId,
                orderName: "장바구니 상품 결제",
                successUrl: window.location.origin + "/payment/success",
                failUrl: window.location.origin + "/payment/fail",
                customerEmail: customerEmail,
                customerName: customerName,
                customerMobilePhone: customerMobilePhone,
                card: {
                    useEscrow: false,
                    flowMode: "DEFAULT",
                    useCardPoint: false,
                    useAppCardOnly: false,
                },
            });
        } catch (error) {
            console.error("결제 요청 중 오류 발생:", error);
            alert("결제 요청 중 오류 발생: " + error.message);
        }
    }

    // 버튼 이벤트 리스너 추가
    const submitButton = document.getElementById("submitBtn");
    if (submitButton) {
        submitButton.addEventListener("click", requestPayment);
    } else {
        console.error("submitBtn 요소를 찾을 수 없습니다.");
    }
});