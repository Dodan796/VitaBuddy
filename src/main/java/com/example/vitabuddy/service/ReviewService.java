package com.example.vitabuddy.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.vitabuddy.dao.IReviewDAO;
import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementStoreVO;

@Service
public class ReviewService implements IReviewService {

	@Autowired
	@Qualifier("IReviewDAO")
	IReviewDAO dao;

	// 특정 제품에 대한 리뷰 조회 기능
	@Override
	public List<ReviewVO> reviewLists(int supId) {
		return dao.reviewLists(supId);
	}

	// 리뷰 작성 기능
	@Override
	public int insertReview(ReviewVO review) {
		return dao.insertReview(review);
	}

	// 리뷰 삭제 기능
	@Override
	public int deleteReview(String reviewNo, String userId) {
		return dao.deleteReview(reviewNo, userId);
	}

	// 리뷰 수정 기능
	@Override
	public int updateReview(ReviewVO review) {
		return dao.updateReview(review);
	}

	// 리뷰 번호를 통한 리뷰 조회
	@Override
	public ReviewVO getReviewByNo(String reviewNo) {
		return dao.getReviewByNo(reviewNo);
	}

	// 특정 사용자에 대한 리뷰 조회 기능
	public List<ReviewVO> getUserReviews(String userId) {
		return dao.getReviewsByUserId(userId);
	}

	// 브랜드별 리뷰순 상품 출력 기능.
	@Override
	public List<SupplementStoreVO> getTopSupplementsByBrand() {
		List<SupplementStoreVO> supplements = dao.getTopSupplementsByBrand();

		for (SupplementStoreVO supplement : supplements) {
			try {
				// SupID에 해당하는 이미지를 byte[]로 가져옴
				// byte[] image = dao.getSupplementImageById(supplement.getSupId()); ** 수정사항1 :
				// 주석처리

				if (supplement.getSupImg() != null) {
					// Base64로 인코딩된 이미지 저장
					String base64Image = Base64.getEncoder().encodeToString(supplement.getSupImg());
					supplement.setBase64SupImg(base64Image);
				} else {
					System.out.println("No image found for SupID " + supplement.getSupId());
				}
			} catch (Exception e) {
				System.err.println("Error fetching image for SupID: " + supplement.getSupId() + " - " + e.getMessage());
				e.printStackTrace();
			}
		}
		return supplements;
	}
	// 이미지 출력 끝

	// 기능별 리뷰순 상품 출력 기능.
	@Override
	public List<SupplementStoreVO> getTopSupplementsByFunction() {
		List<SupplementStoreVO> supplements = dao.getTopSupplementsByFunction();

		for (SupplementStoreVO supplement : supplements) {
			try {
				// SupID에 해당하는 이미지를 byte[]로 가져옴
				// byte[] image = dao.getSupplementImageById(supplement.getSupId()); ** 수정사항1 :
				// 주석처리

				if (supplement.getSupImg() != null) {
					// Base64로 인코딩된 이미지 저장
					String base64Image = Base64.getEncoder().encodeToString(supplement.getSupImg());
					supplement.setBase64SupImg(base64Image);
				} else {
					System.out.println("No image found for SupID " + supplement.getSupId());
				}
			} catch (Exception e) {
				System.err.println("Error fetching image for SupID: " + supplement.getSupId() + " - " + e.getMessage());
				e.printStackTrace();
			}
		}
		return supplements;
	}
	// 이미지 출력 끝

	// 성분별 리뷰순 상품 출력 기능.
	@Override
	public List<SupplementStoreVO> getTopSupplementsByIngredient() {
		List<SupplementStoreVO> supplements = dao.getTopSupplementsByIngredient();

		for (SupplementStoreVO supplement : supplements) {
			try {
				// SupID에 해당하는 이미지를 byte[]로 가져옴
				// byte[] image = dao.getSupplementImageById(supplement.getSupId()); ** 수정사항1 :
				// 주석처리

				if (supplement.getSupImg() != null) {
					// Base64로 인코딩된 이미지 저장
					String base64Image = Base64.getEncoder().encodeToString(supplement.getSupImg());
					supplement.setBase64SupImg(base64Image);
				} else {
					System.out.println("No image found for SupID " + supplement.getSupId());
				}
			} catch (Exception e) {
				System.err.println("Error fetching image for SupID: " + supplement.getSupId() + " - " + e.getMessage());
				e.printStackTrace();
			}
		}
		return supplements;
	}

	// 상위 2개 해시태그 출력.
	@Override
	public List<ReviewVO> getHashtagsByReview(int supId) {
		List<ReviewVO> topHashtags = dao.getHashtagsByReview(supId);
		return topHashtags;
	}

	// 성분별 상위 1개의 상품을 가져오는 기능
	@Override
	public SupplementStoreVO getTopProductByIngredient(String ingredientId) {
		SupplementStoreVO topProduct = dao.getTopProductByIngredient(ingredientId);
		if (topProduct != null) {
			System.out.println("Ingredient ID: " + ingredientId);
			System.out.println("Top Product Name: " + topProduct.getSupName());
		} else {
			System.out.println("No top product found for Ingredient " + ingredientId);
		}
		return topProduct;
	}

	// 전체 리뷰 개수 조회
    @Override
    public int countReviews(int supId) {
        return dao.countReviews(supId);
    }

    // 페이지별 리뷰 3개씩 출력
    private static final int REVIEWS_PER_PAGE = 3; // 페이지당 리뷰 수 상수 정의
    @Override
    public ArrayList<ReviewVO> pagingReviewList(int supId, int page) {
        int offset = (page - 1) * REVIEWS_PER_PAGE;

        Map<String, Integer> pagingParams = new HashMap<>();
        pagingParams.put("supId", supId);
        pagingParams.put("offset", offset);
        pagingParams.put("limit", REVIEWS_PER_PAGE);

        return dao.pagingReviewList(pagingParams);
    }

    // 페이지당 리뷰 수를 반환하는 메서드
    public int getReviewsPerPage() {
        return REVIEWS_PER_PAGE;
    }

}