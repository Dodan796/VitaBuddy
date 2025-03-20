package com.example.vitabuddy.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.example.vitabuddy.dto.KakaoDTO;
import com.example.vitabuddy.model.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.vitabuddy.dto.MemberDTO;
import com.example.vitabuddy.service.MemberService;

@Controller
@RequestMapping("/member") // register.jsp는 /member/register에서 접근 가능하도록 함.
public class MemberController {

    @Autowired
    private MemberService memberService;

    // GET 요청으로 JSP 페이지를 로드할 수 있도록 설정
    @GetMapping("/register")
    public String showRegisterPage() {
        return "member/register"; // /WEB-INF/views/member/register.jsp로 이동
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody MemberDTO member) {
        boolean isRegistered = memberService.registerMember(member);
        Map<String, Object> response = new HashMap<>();

        if (!isRegistered) {
            response.put("success", false);
            response.put("message", "비밀번호 확인이 일치하지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 에러 상태와 메시지를 반환
        }
        
        response.put("success", true);
        response.put("redirect", "/member/login");
        return ResponseEntity.ok(response); // 성공 시 응답과 리다이렉트 URL을 JSON으로 반환
    }

    
    @GetMapping("/checkId")
    public ResponseEntity<Map<String, Boolean>> checkId(@RequestParam String userId) {
        boolean isAvailable = memberService.isUserIdAvailable(userId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "intro"; // /WEB-INF/views/member/intro.jsp로 이동
    }

    @GetMapping("/email")
    public String emailVeri(){
        return "emailVerificationContent";
    }
}