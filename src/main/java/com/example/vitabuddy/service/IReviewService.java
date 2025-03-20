package com.example.vitabuddy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementStoreVO;

public interface IReviewService {

	// 리뷰 조회 기능
	List<ReviewVO> reviewLists(int supId);

	// 리뷰 작성 기능
	int insertReview(ReviewVO review);

	// 리뷰 삭제 기능
	int deleteReview(String reviewNo, String userId);

	// 리뷰 수정 기능
	int updateReview(ReviewVO review);

	// 리뷰 번호를 통한 리뷰 조회.
	ReviewVO getReviewByNo(String reviewNo);

	// 특정 사용자에 대한 리뷰 조회 기능
	List<ReviewVO> getUserReviews(String userId);

	// 브랜드별 상위 1개의 상품을 가져오는 기능/기능별/성분별
	List<SupplementStoreVO> getTopSupplementsByBrand();

	List<SupplementStoreVO> getTopSupplementsByFunction();

	List<SupplementStoreVO> getTopSupplementsByIngredient();

	// 제품별 상위 2개의 해시태그를 가져오는 메서드
	List<ReviewVO> getHashtagsByReview(int supId);

	SupplementStoreVO getTopProductByIngredient(String ingredientId);

	// 페이지네이션을 위한 리뷰 목록 조회 메서드
	public int countReviews(@Param("supId") int supId);
	ArrayList<ReviewVO> pagingReviewList(int supId, int page);
}