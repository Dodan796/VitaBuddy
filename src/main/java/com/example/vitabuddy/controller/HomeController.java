package com.example.vitabuddy.controller;

import com.example.vitabuddy.model.SupplementStoreVO;
import com.example.vitabuddy.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    // 1. 인트로 화면 (로그인화면)
    @RequestMapping("/intro") // intro 추가
    public String intro() {
        return "intro";
    }

    // 2. 메인홈페이지. => 회원용
    @RequestMapping("/")
    public String home(Model model) {

        // 브랜드별 상위 1개의 상품을 가져옴
        //기능별, 성분별 상위 상품 추가
        List<SupplementStoreVO> topSupplementsByBrand = reviewService.getTopSupplementsByBrand();
        List<SupplementStoreVO> topSupplementsByFunction = reviewService.getTopSupplementsByFunction();
        List<SupplementStoreVO> topSupplementsByIngredient = reviewService.getTopSupplementsByIngredient();

        // 모델에 담아 JSP로 전달
        model.addAttribute("topSupplementsBrand", topSupplementsByBrand);
        model.addAttribute("topSupplementsFunction", topSupplementsByFunction);
        model.addAttribute("topSupplementsIngredient", topSupplementsByIngredient);
        return "home";
    }

    //3.메인페이지 => 관리자용
    @RequestMapping("/admin")
    public String adminHome() {
        return "adminHome";
    }

    // 10/26 이미지 출력 - IReviewService 의존성 주입
    @Autowired
    private IReviewService reviewService;

    // 4. 상점 페이지 추가
    @RequestMapping("/supplementList")
    public String supplementList() {
        return "supplement/supplementList"; // supplementList.jsp로 연결
    }

}