package com.example.vitabuddy.controller;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.vitabuddy.model.CartListVO;
import com.example.vitabuddy.model.MemberVO;
import com.example.vitabuddy.dto.UserInfo;
import com.example.vitabuddy.service.CartListService;
import com.example.vitabuddy.service.MemberUpdateService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartListController {

	@Autowired
	CartListService cartListService;

	@Autowired
	MemberUpdateService memService;

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

	@RequestMapping("/supplement/cartList")
	public String cartList(Model model, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return "redirect:/intro";
		}
		ArrayList<CartListVO> cartLists = cartListService.cartList(userId);
		model.addAttribute("cartLists", cartLists);
		return "supplement/cartList";
	}

	@ResponseBody
	@PostMapping("/api/addCart")
	public int addCart(@RequestBody CartListVO vo, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return 0; // 비회원은 실패 처리
		}
		vo.setUserId(userId);

		int result = 0;
		try {
			int count = cartListService.checkSupInCart(vo.getSupId(), userId);
			if (count == 0) {
				vo.setCartQty(vo.getCartQty() + 1);
				cartListService.addCartList(vo);
			} else {
				vo.setCartQty(vo.getCartQty() + 1);
				cartListService.updateQtyInCart(vo);
			}
			result = 1;
		} catch (Exception e) {
			System.out.println("장바구니 처리 중 오류: " + e.getMessage());
		}
		return result;
	}

	@ResponseBody
	@PostMapping("/updateCartQty")
	public int updateCartQty(@RequestParam("cartId") int cartId, @RequestParam("cartQty") int qty, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return 0; // 비회원은 실패 처리
		}
		cartListService.changeQtyInCart(cartId, qty);
		return 1;
	}

	@ResponseBody
	@RequestMapping("/api/deleteCart")
	public int deleteCart(@RequestParam("cartId") String cartId, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return 0;
		}
		if (cartId != null) {
			cartListService.deleteCart(cartId);
			return 1;
		}
		return 0;
	}

	@RequestMapping("/supplement/orderForm")
	public String orderForm(HttpSession session, Model model) {
		String userId = getUserIdFromSessionOrJwt(session);
		if (userId == null) {
			return "redirect:/intro";
		}
		MemberVO myInfo = memService.myInfoUpdateForm(userId);
		model.addAttribute("myInfo", myInfo);

		ArrayList<CartListVO> cartLists = cartListService.cartList(userId);
		model.addAttribute("cartLists", cartLists);

		String orderId = UUID.randomUUID().toString();
		model.addAttribute("orderId", orderId);

		double totalAmount = cartLists.stream()
				.mapToDouble(c -> c.getSupPrice() * c.getCartQty())
				.sum();
		model.addAttribute("totalAmount", totalAmount);

		return "supplement/orderForm";
	}
}
