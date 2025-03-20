package com.example.vitabuddy.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.vitabuddy.dao.ICartListDAO;
import com.example.vitabuddy.model.CartListVO;
import com.example.vitabuddy.model.OrderInfoVO;
import com.example.vitabuddy.model.PurchaseHistoryVO;
import com.example.vitabuddy.model.SupplementStoreVO;


@Service
public class CartListService implements ICartListService {

	@Autowired 
	@Qualifier("ICartListDAO")
	ICartListDAO dao;

	 @Autowired
	 PasswordEncoder pwdEncoder;

	@Override
	public ArrayList<CartListVO> cartList(String userId) {

		ArrayList<CartListVO> cartLists = dao.cartList(userId);

		for (CartListVO cartList : cartLists) {
		    try {
		        if (cartList.getSupImg() != null) {
		            String base64Img = Base64.getEncoder().encodeToString(cartList.getSupImg());
		            cartList.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
		        }
		    } catch (Exception e) {
		        System.out.println("Error encoding image for supplement: " + cartList.getSupName());
		        e.printStackTrace();
		    }
		}


		return cartLists; 
	}

	@Override
	public int checkSupInCart(int supId, String userId) {
		HashMap<String, Object> map = new HashMap<>();
	    map.put("supId", supId);   // int 타입인 supId
	    map.put("userId", userId); // String 타입인 userId
		int count = dao.checkSupInCart(map);
		return count;
	}

	@Override
	public void addCartList(CartListVO vo) {
		dao.addCartList(vo);

	}

	@Override
	public void updateQtyInCart(CartListVO vo) {
		dao.updateQtyInCart(vo);

	}

	@Override
	public int deleteCart(String cartId) {

		return dao.deleteCart(cartId);
	}

	@Override
	public void changeQtyInCart(int cartId, int qty) {
		HashMap<String, Object> map = new HashMap<>();
	    map.put("cartId", cartId);   
	    map.put("qty", qty); 
		dao.changeQtyInCart(map);

	}



	//--------------주문 정보 로직-------------------
	@Override
	public void insertOrderInfo(OrderInfoVO vo) {
		//orderForm.jsp 에서 [주문 상품] & [주문자 정보(배송정보)] -> 분리하기

		//1. 주문자 업데이트 (OrderInfo테이블)
		dao.insertOrderInfo(vo);


		//2. 주문상품 업데이트 (OrderProduct테이블)
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", vo.getOrderId());
		map.put("userId", vo.getUserId());
		dao.insertOrderProduct(map);

		//주문 완료 후, 장바구니에 있는 데이터 삭제
		dao.deleteCartAfterOrder(vo.getUserId());



	}

	@Override
	public ArrayList<PurchaseHistoryVO> getUserPurchaseHistory(String userId) {
		ArrayList<PurchaseHistoryVO> mypagePurchaseLists = dao.getUserPurchaseHistory(userId);

		for (PurchaseHistoryVO mypagePurchaseList : mypagePurchaseLists) {
			String ordDate = mypagePurchaseList.getOrdDate();
			/*String year = orderId.substring(0, 4);  //년도 추출
	        String month = orderId.substring(4, 6);  //월 추출
	        String day = orderId.substring(6, 8);  //일 추출

	        String formattedorderId = year + '-' + month + '-' + day;*/
			mypagePurchaseList.setOrderId(ordDate);  //format된 orderId 로 값 세팅


		}

		//이미지 출력
		for (PurchaseHistoryVO mypagePurchaseList : mypagePurchaseLists) {
			try {
				if (mypagePurchaseList.getSupImg() != null) {
					String base64Img = Base64.getEncoder().encodeToString(mypagePurchaseList.getSupImg());
					mypagePurchaseList.setBase64SupImg(base64Img); // Base64로 인코딩된 이미지 설정
				}
			} catch (Exception e) {
				System.out.println("Error encoding image for supplement: " + mypagePurchaseList.getSupName());
				e.printStackTrace();
			}
		}



		return mypagePurchaseLists;




	}
}