package com.example.vitabuddy.controller;

import java.util.List;

import com.example.vitabuddy.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.model.MemberVO;
import com.example.vitabuddy.service.MemberUpdateService;
import com.example.vitabuddy.service.SupplementService;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberUpdateController {

	@Autowired
	MemberUpdateService memService;

	@Autowired

	SupplementService supService;

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

	// 마이페이지 > 회원정보 수정폼으로 이동
	@RequestMapping("/member/myInfoChangeForm")
	public String myInfoChangeForm(HttpSession session, Model model) {

		String userId = getUserIdFromSessionOrJwt(session);
		MemberVO myInfo = memService.myInfoUpdateForm(userId);
		model.addAttribute("myInfo", myInfo);

		// 복용 중인 영양제 리스트를 가져옴 => 추가된 코드.
		List<UserSupplementDTO> userSupplements = supService.getUserSupplements(userId);
		model.addAttribute("userSupplements", userSupplements);

		return "member/infoChange"; // JSP 파일 이름에 맞게 경로 수정
	}

	// 회원 정보 수정 처리
	@RequestMapping("/member/myInfoUpdate")
	public String myInfoUpdate(MemberVO vo, @RequestParam("userPh1") String userPh1,
			@RequestParam("userPh2") String userPh2, @RequestParam("userPh3") String userPh3, HttpSession session) {
		String userId = getUserIdFromSessionOrJwt(session);
		vo.setUserId(userId); // vosetUserId 추가 1017

		String userPh = userPh1 + "-" + userPh2 + "-" + userPh3;
		vo.setUserPh(userPh);
		memService.myInfoUpdate(vo);
		return "redirect:/member/myPage"; // 수정 후 마이페이지로 리다이렉트
	}

}