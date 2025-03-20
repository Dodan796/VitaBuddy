package com.example.vitabuddy.controller;

import com.example.vitabuddy.dto.GoogleDTO;
import com.example.vitabuddy.service.GoogleService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class GoogleController {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Autowired
    GoogleService googleService;
    @GetMapping("/member/socialLogin/google")
    public void redirectToGoogleLogin(HttpServletResponse response) throws IOException {
        String authorizationUrl = "https://accounts.google.com/o/oauth2/v2/auth?" +
                "client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20email%20profile";
        response.sendRedirect(authorizationUrl);
    }


    // Google OAuth2 로그인 콜백
    @RequestMapping("/member/socialLogin/google/callback")
    public String googleLogin(@RequestParam("code") String code, HttpSession session) throws IOException {
        // Authorization Code 로그 출력
        System.out.println("Authorization Code: " + code);

        // 1. Access Token 요청
        String accessToken = googleService.getAccessToken(code);
        System.out.println("Access Token: " + accessToken);

        // 2. 사용자 정보 요청
        HashMap<String, Object> userInfo = googleService.getGoogleUserInfo(accessToken);

        // 사용자 정보 출력 (디버깅)
        String userId = (String) userInfo.get("userId");
        String userName = (String) userInfo.get("userName");
        String userEmail = (String) userInfo.get("userEmail");

        System.out.println("User ID: " + userId);
        System.out.println("User Name: " + userName);
        System.out.println("User Email: " + userEmail);

        //수정1. 구글 서버에서 가져온 userInfo 를 googleDTO 에 저장
        GoogleDTO googleDTO = new GoogleDTO();
        googleDTO.setUserId(userId);
        googleDTO.setUserName(userName);
        googleDTO.setUserEmail(userEmail);
        googleDTO.setAuthType("google");

        //서버에 해당 userId가 존재하는지 반환 (true, false)
        GoogleDTO existingUser = googleService.checkExistingUser(userId);

        if (existingUser != null) {
            // 이메일이 이미 등록되어 있으면 로그인 처리
            System.out.println("Existing user logged in: " + userId);
            // 세션에 sid(사용자 ID) 저장
            session.setAttribute("sid", existingUser.getUserId());
            return "redirect:/"; // 로그인 성공 후 이동할 페이지
        } else {
            // 이메일이 등록되어 있지 않으면 회원가입 처리 (사용자 등록 - 서비스 호출)
            googleService.insertGoogleMember(googleDTO);  //DB저장하는 코드
            session.setAttribute("sid", googleDTO.getUserId()); //수정2. 회원가입도 session 세팅해줘야 함
            return "redirect:/";

        }
    }
}
