package com.example.vitabuddy.controller;

import com.example.vitabuddy.dto.UserInfo;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.vitabuddy.model.WishListVO;
import com.example.vitabuddy.service.WishListService;

@Controller
@RequestMapping("/supplement")
public class WishListController {

	@Autowired
	private WishListService wishService;

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

	// 1. 찜 목록 조회
	@GetMapping("/wishList")
	public String getWishList(HttpSession session, Model model) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return "redirect:/intro";
		}
		model.addAttribute("wishList", wishService.getWishList(userId));
		return "supplement/wishList";
	}

	// 2. 찜 목록에 상품 추가
	@ResponseBody
	@PostMapping("/wishList/insert")
	public ResponseEntity<String> insertWishList(@RequestBody WishListVO wishListVO, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return ResponseEntity.status(401).body("Unauthorized");
		}
		wishListVO.setUserId(userId);
		wishService.insertWishList(wishListVO);
		return ResponseEntity.ok("찜 목록에 추가되었습니다.");
	}

	// 3. 찜 목록에서 상품 삭제
	@ResponseBody
	@PostMapping("/wishList/delete")
	public int deleteWishList(@RequestBody HashMap<String, Object> requestBody, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return 0;
		}
		int supId = (int) requestBody.get("supId");

		int result = wishService.deleteWishList(supId, userId);
		return result > 0 ? 1 : 0;
	}

	// 4. 찜 목록에서 장바구니로 상품 추가
	@ResponseBody
	@PostMapping("/wishList/toCart")
	public int addWishListtoCartList(@RequestBody HashMap<String, Object> toCart, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return 0;
		}
		int supId = (int) toCart.get("supId");
		int result = wishService.addWishListtoCartList(supId, userId);
		return result > 0 ? 1 : 0;
	}
}
