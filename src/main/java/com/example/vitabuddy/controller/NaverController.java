package com.example.vitabuddy.controller;

import com.example.vitabuddy.dto.NaverDTO;
import com.example.vitabuddy.service.NaverService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

@Controller
@RequestMapping("/member/socialLogin")
public class NaverController {

    @Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.redirect.uri}")
    private String redirectUri;

    @Autowired
    private NaverService naverService;

    @GetMapping("/naver")
    public void redirectToNaverLogin(HttpServletResponse response) throws IOException {
        String state = UUID.randomUUID().toString();
        String authorizationUrl = "https://nid.naver.com/oauth2.0/authorize?" +
                "response_type=code" +
                "&client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&state=" + state;
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/naver/callback")
    public String naverLogin(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session) throws IOException {
        System.out.println("Authorization Code: " + code);

        String accessToken = naverService.getAccessToken(code, state);
        System.out.println("Access Token: " + accessToken);

        HashMap<String, Object> userInfo = naverService.getUserInfo(accessToken);

        String userId = (String) userInfo.get("id");
        String userName = (String) userInfo.get("name");
        String userEmail = (String) userInfo.get("email");

        NaverDTO existingUser = naverService.findUserById(userId);

        if (existingUser != null) {
            session.setAttribute("sid", existingUser.getUserId());
            System.out.println("세션에 저장된 SID: " + session.getAttribute("sid"));  // **로그 추가**
            return "redirect:/";
        } else {
            NaverDTO naverDTO = new NaverDTO();
            naverDTO.setUserId(userId);
            naverDTO.setUserName(userName);
            naverDTO.setUserEmail(userEmail);
            naverDTO.setGender((String) userInfo.get("gender"));
            naverDTO.setBirthYear((String) userInfo.get("birthyear"));

            naverService.registerNaverMember(naverDTO);
            session.setAttribute("sid", userId);
            System.out.println("신규 가입 사용자 세션 저장: " + session.getAttribute("sid"));  // **로그 추가**
            return "redirect:/";
        }
    }


}