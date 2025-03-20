package com.example.vitabuddy.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.vitabuddy.model.WishListVO;

public interface IWishListService {
	// 1. 찜 목록 조회
	ArrayList<WishListVO> getWishList(String userId);

	// 2. 찜 목록 추가
	void insertWishList(WishListVO wishListVO);

	// 3. 찜 목록 삭제
	public int deleteWishList(int supId, String userId);

	// 4. 찜 목록에서 장바구니로 상품 추가
	public int addWishListtoCartList(int supId, String userId);
}