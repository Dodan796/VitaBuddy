package com.example.vitabuddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.vitabuddy.service.LoginService;
import jakarta.servlet.http.HttpSession;
@Controller
public class LoginController {
    //@Autowired
    //LoginService logService;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);


    // 1. 로그인 기능
    /*@ResponseBody
    @RequestMapping("/intro/login")
//    public String loginCheck(@RequestParam HashMap<String, Object> param, HttpSession session) {
//        String result = logService.login(param);
//
//        if(result.equals("success")) {
//            // 세션에 userId를 sid로 저장
            session.setAttribute("sid", param.get("id"));
            return "success";
        } 
        return "fail";
    }*/

    // 로그아웃 기능 - 소셜 로그인용
    @RequestMapping("/member/logout")
    public String logout(HttpSession session) {

        //로그아웃 클릭 시, 세션이 잘 삭제되었는지 확인하기 위한 로직
        // 로그아웃 전 세션 데이터 확인
        System.out.println("Before invalidating session: " +
                (session.getAttributeNames().hasMoreElements() ? "Session data exists" : "No session data"));

        session.invalidate(); // 세션 무효화

        // 로그아웃 후 세션 상태 확인 - invalidate 한 이후, 세션에 있는 데이터 값 자체를 불러올 수 없음.
        System.out.println("Session invalidated successfully.");

        return "redirect:/intro"; // 로그아웃 후 intro 화면으로 이동
    }





}
