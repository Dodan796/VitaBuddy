package com.example.vitabuddy.controller;

import com.example.vitabuddy.service.EmailVerificationService;
import com.example.vitabuddy.service.MemberService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class EmailVerificationController {

    // 주입
    private final EmailVerificationService emailVerificationService;
    private final MemberService memberService;

    // 이메일 보내는 로직.
    @RequestMapping("/member/emailverify")
    @ResponseBody
    public String emailVerification(@RequestParam("userEmail") String userEmail) throws MessagingException, IOException {
        String code = emailVerificationService.sendSimpleMessage(userEmail);
        System.out.println("이메일인증 코드입니다" + code);
        return code;
    }

    // 이메일 인증번호 검증 로직
    @RequestMapping("/member/verifyCode")
    @ResponseBody
    public boolean verifyCode(@RequestParam("userEmail") String userEmail, @RequestParam("code") String userInputCode) {
        System.out.println("사용자가 입력한 이메일: " + userEmail);
        System.out.println("사용자가 입력한 인증코드: " + userInputCode);

        boolean isVerified = emailVerificationService.verifyCode(userEmail, userInputCode);
        if (isVerified) {
            System.out.println("이메일 인증 성공: " + userEmail);
        } else {
            System.out.println("이메일 인증 실패: " + userEmail);
        }
        return isVerified;
    }


    //이메일 중복체크
    @ResponseBody
    @GetMapping("/member/checkEmail")
    public String emailCheckfromDB(@RequestParam("userEmail") String userEmail){

        boolean isuserEmailAvailable = memberService.isUserEmailAvailable(userEmail);  //true, false
        if(isuserEmailAvailable) {
            return "1";
        }
        return "0";

    }
}