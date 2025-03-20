package com.example.vitabuddy.controller;

import com.example.vitabuddy.dto.MemberDTO;
import com.example.vitabuddy.dto.UserSupplementDTO;
import com.example.vitabuddy.service.MemberService;
import com.example.vitabuddy.service.SupplementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
@RequestMapping("/member")
public class InfoChangeController {

    private final MemberService memberService;
    private final SupplementService supplementService;

    @Autowired
    public InfoChangeController(MemberService memberService, SupplementService supplementService) {
        this.memberService = memberService;
        this.supplementService = supplementService;
    }

    // 회원 정보와 복용 중인 영양제 목록 조회
    @GetMapping("/infoChange")
    public String getUserInfo(Model model, @RequestParam String userId) {
        // 회원 정보 조회
        MemberDTO memberInfo = memberService.getUserInfo(userId);
        
        // 사용자가 복용 중인 영양제 목록 조회
        List<UserSupplementDTO> userSupplements = supplementService.getUserSupplements(userId);

        // Model에 데이터를 추가하여 JSP로 전달
        model.addAttribute("memberInfo", memberInfo);
        model.addAttribute("userSupplements", userSupplements);

        // infoChange.jsp로 이동
        return "member/infoChange";
    }
}
