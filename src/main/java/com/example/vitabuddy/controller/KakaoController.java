package com.example.vitabuddy.controller;


import com.example.vitabuddy.dto.KakaoDTO;
import com.example.vitabuddy.service.KakaoService;
import com.example.vitabuddy.service.MemberService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.HashMap;

@Controller
public class KakaoController {

    @Autowired
    KakaoService kakaoService;

    @Autowired
    private HttpSession session;

    //카카오 로그인
    //jsp(프론트)에서 인가 코드 요청 -> 로그인(동의버튼클릭) 이후, url보면 code가 포함되어있는 링크가 보인다
    //여기서 callback url로 code를 받아와서 아래 로직 작성.
    @RequestMapping("/oauth/kakao/callback")
    public String kakaocallback(@RequestParam("code") String code, HttpSession session) throws IOException {

        String usedCode = (String) session.getAttribute("usedCode");
        if(usedCode != null && usedCode.equals(code)){
            System.out.println("이미 처리된 인가코드입니다");
            return "home";
        }

        //인가코드 세션에 저장
        session.setAttribute("usedCode", code);

        //액세스 토큰 요청
        System.out.println("인가코드는 : " + code + " 입니다.");  //인가 코드 로그 출력

        try {
            HashMap<String, Object> tokens = kakaoService.getAccessToken(code);
            String access_token = (String) tokens.get("access_token");  //액세스 토큰
            String refresh_token = (String) tokens.get("refresh_token");  //리프레시 토큰
            int expires_in = (int) tokens.get("expires_in");  //액세스 토큰 만료일
            int refresh_token_expires_in = (int) tokens.get("refresh_token_expires_in");  //리프레시 토큰 만료일

            //로그 확인용
            System.out.println("토큰은 : " + access_token + " 입니다.");
            System.out.println("리프레시 토큰은 : " + refresh_token + " 입니다.");
            System.out.println("만료일은 : " + expires_in + " 입니다.");
            System.out.println("리프레시 토큰 만료일 : " + refresh_token_expires_in + " 입니다.");

            //access_token을 세션에 저장 - 토큰 휘발 방지
            session.setAttribute("accessToken", access_token);
            session.setAttribute("refreshToken", refresh_token);


            //사용자 정보 가져오기
            HashMap<String, Object> userInfo = kakaoService.getkakaoUserInfo(access_token);

            //UserId
            String userId = (String) userInfo.get("userId");
            System.out.println("사용자 정보는 : " + userInfo);  //로그 출력 확인

            //UserName
            String userName = (String) userInfo.get("userName");
            System.out.println("사용자 이름은 : " + userName);

            //UserEmail
            String userEmail = (String) userInfo.get("userEmail");
            System.out.println("사용자 이메일은 : " + userEmail);

            //KakaoDTO에 가져온 데이터 저장
            KakaoDTO kakaoDTO = new KakaoDTO();  //클래스 생성
            kakaoDTO.setUserId(userId);
            kakaoDTO.setUserName(userName);
            kakaoDTO.setUserEmail(userEmail);


            //DB에 userEmail이 존재하는지 확인
            Boolean isUserExists = kakaoService.findByUserId(kakaoDTO.getUserId());
            System.out.println(isUserExists);

            if (isUserExists) {
                //바로 로그인하는 로직
                String user = kakaoService.getUserById(kakaoDTO.getUserId());
                session.setAttribute("sid", user);
                System.out.println("로그인만 성공입니다");
            } else {
                kakaoService.registerkakaoMember(kakaoDTO);
                session.setAttribute("sid", kakaoDTO.getUserId());  // 수정사항 : kakaoDTO를 sid로 설정하면 userId를 불러올 수가 없으므로 getUserId를 무조건 설정
                System.out.println("회원가입 + 로그인 성공입니다");
            }

        }catch (Exception e){
            System.out.println("토큰 처리 중 오류 발생" + e.getMessage());
            throw e;
        }

        return "home";  //home.jsp

    }


}
