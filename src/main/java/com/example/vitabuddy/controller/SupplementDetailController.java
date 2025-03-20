package com.example.vitabuddy.controller;

import com.example.vitabuddy.dto.UserInfo;
import com.example.vitabuddy.model.ReviewVO;
import com.example.vitabuddy.model.SupplementDetailVO;
import com.example.vitabuddy.service.ReviewService;
import com.example.vitabuddy.service.SupplementDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/api/supplement")
public class SupplementDetailController {

    @Autowired
    private SupplementDetailService supplementDetailService;

    @Autowired
    private ReviewService reviewService;

    // GET: 상품 상세 + 리뷰 목록 페이지 (비회원도 조회 가능)
    @GetMapping("/supplementDetail/{id}")
    public String getSupplementDetail(
            @PathVariable("id") int supplementDetailId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model,
            HttpSession session
    ) {
        // 1. 보충제 상세 정보 조회 및 이미지 Base64 인코딩 처리
        SupplementDetailVO supplementDetail = supplementDetailService.getSupplementDetailById(supplementDetailId);
        if (supplementDetail.getSupImg() != null) {
            String base64Image = Base64.getEncoder().encodeToString(supplementDetail.getSupImg());
            model.addAttribute("supImgBase64", base64Image);
        } else {
            model.addAttribute("supImgBase64", "");
        }
        model.addAttribute("supplementDetail", supplementDetail);

        // 2. 리뷰 목록 및 페이지네이션 처리
        List<ReviewVO> reviews = reviewService.pagingReviewList(supplementDetailId, page);
        model.addAttribute("reviewList", reviews);
        int totalReviews = reviewService.countReviews(supplementDetailId);
        int totalPages = (int) Math.ceil((double) totalReviews / reviewService.getReviewsPerPage());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        // 3. 상위 해시태그 (optional)
        List<ReviewVO> topHashtags = reviewService.getHashtagsByReview(supplementDetailId);
        model.addAttribute("topHashtags", topHashtags);

        // 4. 현재 로그인 사용자 식별 (세션 또는 JWT)
        String myUserId = getUserIdFromSessionOrJwt(session);
        model.addAttribute("myUserId", myUserId);

        return "supplement/supplementDetail";
    }

    // GET: AJAX 요청으로 리뷰 목록을 JSON으로 반환 (비회원도 조회 가능)
    @GetMapping("/supplementDetail/{id}/reviews")
    @ResponseBody
    public Map<String, Object> getReviews(
            @PathVariable("id") int supplementDetailId,
            @RequestParam(value = "page", defaultValue = "1") int page
    ) {
        List<ReviewVO> reviews = reviewService.pagingReviewList(supplementDetailId, page);
        int totalReviews = reviewService.countReviews(supplementDetailId);
        int totalPages = (int) Math.ceil((double) totalReviews / reviewService.getReviewsPerPage());
        Map<String, Object> response = new HashMap<>();
        response.put("reviewList", reviews);
        response.put("currentPage", page);
        response.put("totalPages", totalPages);
        return response;
    }

    // 헬퍼: 세션 또는 JWT(SecurityContextHolder)에서 현재 로그인 사용자 식별자(userId 또는 userEmail)를 반환
    private String getUserIdFromSessionOrJwt(HttpSession session) {
        // 1. 세션 기반 로그인
        String userId = (String) session.getAttribute("sid");
        if (userId != null) {
            return userId;
        }
        // 2. JWT 기반 로그인: SecurityContextHolder에서 인증 정보를 조회
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            if (auth.getPrincipal() instanceof UserInfo) {
                return ((UserInfo) auth.getPrincipal()).getUsername();
            }
            return auth.getName();
        }
        return null;
    }
}
