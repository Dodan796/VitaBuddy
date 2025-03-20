window.onload = function () {

	
	// 수정사항 : 페이지가 로드될 때(=장바구니 페이지로 처음 접속했을 때), 상품별 주문금액(수량*단가) 와 총 금액 계산
	const qtyInputs = document.querySelectorAll('.cartQty');
	qtyInputs.forEach((input, index) => {
		let qty = parseInt(input.value);
		calAmount(index, qty);
	
	});

	updateTotalAmount();
	

    const plusButtons = document.querySelectorAll('.plusBtn');
    const minusButtons = document.querySelectorAll('.minusBtn');
    
    plusButtons.forEach((plusBtn, index) => {
        plusBtn.addEventListener('click', () => {
            qtyChange(index, 1);
        });
    });

    minusButtons.forEach((minusBtn, index) => {
        minusBtn.addEventListener('click', () => {
            qtyChange(index, -1);
        });
    });

   
    function qtyChange(index, num) {  //수량 변경하는 필드
        // 현재 선택된 수량 필드와 금액 필드
        const qtyInputs = document.querySelectorAll('.cartQty');
        let qty = parseInt(qtyInputs[index].value);
        qty = qty + num;

        if (qty < 1) qty = 1;  // 1보다 작아지지 않도록

        calAmount(index, qty);
        
        //ajax 로 통신 -> cartId, qty 전송하여 - 서버에 수량 업데이트
        const cartId = document.querySelectorAll('.deleteCartBtn')[index].dataset.cartId;  // 각 상품의 cartId가 있다고 가정
        console.log(cartId);
    	updateCartQty(cartId, qty);


        
    } 
    
    function updateCartQty(cartId, qty) {
	    $.ajax({
	        url: "/updateCartQty",
	        method: "POST",
	        data: { cartId: cartId, cartQty: qty },
	        success: function(response) {
	            console.log("수량 업데이트 성공");
	        },
	        error: function(xhr, status, error) {
	            console.log("수량 업데이트 실패:", error);
	        }
	    });
	}
    

    function calAmount(index, qty) {
        const qtyInputs = document.querySelectorAll('.cartQty');   //수량 
        const priceElements = document.querySelectorAll('.price');    //단가
        const amountElements = document.querySelectorAll('.amount');   //주문금액 

        const price = parseInt(priceElements[index].dataset.price); // dataset에서 가격 추출 - 단가
        const totalAmount = qty * price;
       
        // 개별 상품 금액
        qtyInputs[index].value = qty;
        amountElements[index].innerHTML = totalAmount.toLocaleString(); // 천단위 구분자 

        // 총 금액
        updateTotalAmount();
    }
    


    function updateTotalAmount() {
        let total = 0;

        document.querySelectorAll('.amount').forEach(amountElement => {
            total += parseInt(amountElement.textContent.replace(/,/g, '')); // , 제거 후 숫자로 변환해서 계산
        });

        // 총 주문 금액 
        const totalElement = document.querySelector('#total'); 
        totalElement.innerHTML = total.toLocaleString(); 
    }
};