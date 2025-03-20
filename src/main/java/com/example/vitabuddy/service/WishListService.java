package com.example.vitabuddy.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.vitabuddy.dao.IWishListDAO;
import com.example.vitabuddy.model.WishListVO;

@Service
public class WishListService implements IWishListService {

	@Autowired
	IWishListDAO dao;

	// 1. 찜 목록 조회
	@Override
	public ArrayList<WishListVO> getWishList(String userId) {
		ArrayList<WishListVO> wishList = dao.getWishList(userId);

		// 이미지 데이터 Base64 변환
		for (WishListVO wish : wishList) {
			if (wish.getSupImg() != null) {
				String base64Image = Base64.getEncoder().encodeToString(wish.getSupImg());
				wish.setBase64SupImg(base64Image);
			}
		}

		return wishList;
	}

	// 2. 찜 목록에 상품 추가 (SQL 레벨에서 중복 체크)
	@Override
	public void insertWishList(WishListVO wishListVO) {
		// 중복 여부를 SQL에서 확인 (존재하면 1, 존재하지 않으면 0)
		int isDuplicate = dao.checkDuplicateWish(wishListVO.getUserId(), wishListVO.getSupId());

		// 중복이 아니면 찜 목록에 추가
		if (isDuplicate == 0) {
			dao.insertWishList(wishListVO);
		}
	}

	

	// 3. 찜 목록에서 상품 삭제
	@Override
    public int deleteWishList(int supId, String userId) {
        HashMap<String, Object> delete = new HashMap<>();
        delete.put("supId", supId);
        delete.put("userId", userId);
        
        return dao.deleteWishList(delete);
    }

	// 4. 찜 목록에서 장바구니로 상품 추가.
	@Override
	public int addWishListtoCartList(int supId, String userId) {
		HashMap<String, Object> toCart = new HashMap<>();
		toCart.put("supId", supId);
		toCart.put("userId", userId);
		
		return dao.addWishListtoCartList(toCart);
	}
	
	

}