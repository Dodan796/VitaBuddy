package com.example.vitabuddy.service;

import java.util.ArrayList;

import com.example.vitabuddy.model.CartListVO;
import com.example.vitabuddy.model.OrderInfoVO;
import com.example.vitabuddy.model.PurchaseHistoryVO;

public interface ICartListService {

	// 장바구니 목록 조회
	public ArrayList<CartListVO> cartList(String userId);

	// 현재 장바구니에 있는 상품 갯수 반환
	// 사용자가 추가하고자 하는 상품과 장바구니 페이지에 동일한 상품이 존재하면(개수가 0이 아니라면) "수량 증가"하고, 개수가 0이라면
	// 존재하지 않으므로 "상품 추가"
	public int checkSupInCart(int supId, String userId);

	// 새로 장바구니 추가
	public void addCartList(CartListVO vo);

	// 장바구니 수량 업데이트
	public void updateQtyInCart(CartListVO vo);

	// 장바구나 상품 삭제 - 삭제된 행 갯수 반환 (Controller에서 로직 처리)
	public int deleteCart(String cartId);

	// 장바구니 수량 업데이트 (+/- 버튼 클릭 시, 변경된 수량이 바로 서버로 업데이트 되도록)
	public void changeQtyInCart(int cartId, int qty);

	// -------------------주문 정보
	public void insertOrderInfo(OrderInfoVO vo);

	// 10/28 마이페이지 구매내역 출력
	ArrayList<PurchaseHistoryVO> getUserPurchaseHistory(String userId);
}