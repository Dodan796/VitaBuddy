package com.example.vitabuddy.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.vitabuddy.dto.UserInfo;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.model.CartListVO;
import com.example.vitabuddy.model.InteractionVO;
import com.example.vitabuddy.model.PurchaseHistoryVO;
import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.service.CartListService;
import com.example.vitabuddy.service.IReviewService;
import com.example.vitabuddy.service.InteractionService;
import com.example.vitabuddy.model.RecommendVO;
import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.service.RecommendService;
import com.example.vitabuddy.service.SupplementService;

@Controller
@RequestMapping("/member")
public class MyPageController {

	@Autowired
	private SupplementService supService;

	@Autowired
	private IReviewService reviewService;

	@Autowired
	private InteractionService intService;

	@Autowired
	private RecommendService recommendService; // 영양제의 추천 성분을 위한 서비스 주입

	@Autowired
	private CartListService cartService;

	/**
	 * JWT 또는 세션을 통해 현재 인증된 사용자 ID(이메일)를 얻어오는 메서드
	 */
	private String getUserIdFromSessionOrJwt(HttpSession session) {
		String userId = (String) session.getAttribute("sid");
		if (userId != null) {
			return userId;
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserInfo) {
			UserInfo userInfo = (UserInfo) auth.getPrincipal();
			return userInfo.getUsername();
		}
		return null;
	}

	// 마이페이지로 이동
	@GetMapping("/myPage")
	public String myPage(HttpSession session, Model model) {
		// 세션에서 사용자 ID를 가져옴
		// JWT나 세션으로부터 userId(또는 userEmail) 가져오기
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			// 인증되지 않은 경우
			return "redirect:/intro";
		}

		// 사용자가 복용 중인 영양제 목록을 조회
		List<UserSupplementDTO> userSupplements = supService.getUserSupplements(userId);
		model.addAttribute("userSupplements", userSupplements);

		// 상호작용 정보 조회
		List<String> ingredientNames = intService.getIngredientNames(userId);
		List<InteractionVO> interactions = intService.getInteractionDetails(userId);
		System.out.println("Interactions 데이터: " + interactions);

		model.addAttribute("ingredientNames", ingredientNames);
		model.addAttribute("interactions", interactions);

		// ----------------------------------------------------------------
		// 영양제 성분 추천
		// 1. 사용자가 선택한 영양제 목록의 "주성분" 조회
		ArrayList<RecommendVO> recommendIngredientList = recommendService.recommendIngredients(userId);
		// model.addAttribute("recommendIngredientList", recommendIngredientList);
		for (RecommendVO recommendIngredient : recommendIngredientList) {
			System.out.println("사용자가 선택한 영양제입니다 : " + recommendIngredient.getIngredient());
		}

		Map<Object, ArrayList<RecommendVO>> allRecommendLists = new HashMap<>();

		for (int i = 0; i < recommendIngredientList.size(); i++) {
			String ingredientId = recommendIngredientList.get(i).getIngredientId();
			System.out.println("Processing ingredientId : " + ingredientId);

			// 각 영양제에 대해 추천 성분 리스트를 가져옴
			ArrayList<RecommendVO> recommendLists = recommendService.interactionRecommend(ingredientId);
			System.out.println("서비스계층 테스트 출력" + recommendLists);

			for (RecommendVO recommendVO : recommendLists) {
				System.out.println("Ingredients: " + recommendVO.getIngredients());
				System.out.println("Interaction Recommend: " + recommendVO.getInteractionRecommend());
			}
			// Map에 추천 리스트 저장
			allRecommendLists.put(i, recommendLists);

			System.out.println("Recommendations for ingredient " + ingredientId + " : " + recommendLists);
		}

		// JSP에 Map을 전달
		model.addAttribute("allRecommendLists", allRecommendLists);
		System.out.println("allRecommendList 테스트 출력" + allRecommendLists);
		model.addAttribute("recommendIngredientList", recommendIngredientList);

		// ----------------------------------------------------------------

		// 사용자가 작성한 리뷰 목록을 조회
		List<ReviewVO> userReviews = reviewService.getUserReviews(userId);

		model.addAttribute("reviews", userReviews);



		//10/28 구매내역 출력 (주문일자, 이미지, 상품 정보(상품명, 브랜드, 가격), 수량, 금액)
		ArrayList<PurchaseHistoryVO> myPagePurchaseLists = cartService.getUserPurchaseHistory(userId);

		/*LocalDate today = LocalDate.of(2025, 2, 27);  // 날짜 설정*/
		LocalDate today = LocalDate.now(); // 현재 시스템 날짜
		LocalDate oneMonthAgo = today.minusMonths(1);  //1개월 전
		LocalDate threeMonthsAgo = today.minusMonths(3);  //3개월 전

		ArrayList<PurchaseHistoryVO> recentPurchases = new ArrayList<>();  //1개월 구매내역
		ArrayList<PurchaseHistoryVO> midTermPurchases = new ArrayList<>();  //1~3개월 구매내역
		ArrayList<PurchaseHistoryVO> oldPurchases = new ArrayList<>();  //3개월 구매내역


		for (PurchaseHistoryVO myPagePurchaseList : myPagePurchaseLists) {
			String dateString = myPagePurchaseList.getOrdDate(); // String 날짜

			try {
				// 문자열을 LocalDate로 변환
				LocalDate orderDate = LocalDate.parse(dateString.substring(0, 10)); // yyyy-MM-dd만 추출 후 변환

				if (orderDate.isAfter(oneMonthAgo)) {
					recentPurchases.add(myPagePurchaseList); // 1개월 이내
				} else if (orderDate.isAfter(threeMonthsAgo)) {
					midTermPurchases.add(myPagePurchaseList); // 1~3개월
				} else {
					oldPurchases.add(myPagePurchaseList); // 3개월 이전
				}
			} catch (Exception e) {
				System.err.println("Error parsing date: " + dateString);
			}


		}
		model.addAttribute("recentPurchases", recentPurchases);
		model.addAttribute("midTermPurchases", midTermPurchases);
		model.addAttribute("oldPurchases", oldPurchases);

		return "member/myPage";
	}
}