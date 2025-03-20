package com.example.vitabuddy.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.vitabuddy.model.WishListVO;

public interface IWishListDAO {

	// 1. 찜 목록 조회
	ArrayList<WishListVO> getWishList(String userId);

	// 2. 찜 목록 추가
	void insertWishList(WishListVO wishListVO);

	// 3.찜 목록 중복 확인 
	int checkDuplicateWish(String userId, int supId);

	// 4. 찜 목록 삭제 (supId와 userId 기준으로 삭제)
	public int deleteWishList(HashMap<String, Object> delete);
	
	// 5. 찜 목록에서 장바구니로 상품 추가
	public int addWishListtoCartList(HashMap<String, Object> toCart);

}